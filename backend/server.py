import os, json, statistics
from datetime import datetime, timedelta, timezone
from typing import Optional, Dict, Any
from fastapi import FastAPI, HTTPException, Header, Depends
from pydantic import BaseModel, field_validator
from dotenv import load_dotenv
from openai import OpenAI
import firebase_admin
from firebase_admin import credentials, auth
from google.cloud import firestore

# ----- env & clients -----
load_dotenv()
if not firebase_admin._apps:
    svc_json = os.getenv("FIREBASE_SERVICE_ACCOUNT_JSON")
    if not svc_json:
        raise RuntimeError("FIREBASE_SERVICE_ACCOUNT_JSON mancante")
    cred = credentials.Certificate(json.loads(svc_json))
    firebase_admin.initialize_app(cred, {
        "projectId": os.getenv("FIREBASE_PROJECT_ID")
    })
db = firestore.Client(project=os.getenv("FIREBASE_PROJECT_ID"))

OPENAI_API_KEY = os.getenv("OPENAI_API_KEY")
if not OPENAI_API_KEY:
    raise RuntimeError("OPENAI_API_KEY mancante")
client = OpenAI(api_key=OPENAI_API_KEY)

app = FastAPI(title="Selfjudge API")

# ----- models -----
class JudgeReq(BaseModel):
    artist: str
    text: str
    @field_validator("artist","text")
    @classmethod
    def not_empty(cls, v):
        v = (v or "").strip()
        if not v: raise ValueError("Campo richiesto")
        return v

class BattleReq(BaseModel):
    artist: str
    text_a: str
    text_b: str

# ----- auth -----
def get_uid(authorization: Optional[str] = Header(None)) -> str:
    if not authorization or not authorization.lower().startswith("bearer "):
        raise HTTPException(401, "Token mancante")
    token = authorization.split(" ", 1)[1]
    try:
        decoded = auth.verify_id_token(token)
        return decoded["uid"]
    except Exception:
        raise HTTPException(401, "Token non valido")

# ----- helpers -----
STYLE_NOTICE = ("Se l'artista è vivente/protetto NON imitare 1:1: "
                "usa solo il 'vibe' generale senza frasi distintive o citazioni.")

PROMPT = """Sei "Selfjudge". Valuta il testo in 5 voci:

1) Psichiatra — emozioni, coerenza interna, difese; 2–4 frasi. Voto 0–10.
2) Produttore musicale — orecchiabilità/metrica/flow, hook, originalità; 2–4 frasi. Voto 0–10.
3) Critico letterario — stile, immagini, ritmo, figure retoriche; 2–4 frasi. Voto 0–10.
4) Ascoltatore medio — impressione istintiva, chiarezza, memorabilità; 1–3 frasi. Voto 0–10.
5) Artista preferito: "{artist} (vibe)" — {style_notice}; 2–4 frasi. Voto 0–10.

Scala: 0–2 pessimo, 3–4 debole, 5–6 discreto, 7–8 buono, 9–10 eccellente.
Rispondi SOLO in JSON:
{{
  "judgments": [
    {{"persona":"Psichiatra","score":<0-10>,"comment":"..."}},
    {{"persona":"Produttore musicale","score":<0-10>,"comment":"..."}},
    {{"persona":"Critico letterario","score":<0-10>,"comment":"..."}},
    {{"persona":"Ascoltatore medio","score":<0-10>,"comment":"..."}},
    {{"persona":"{artist} (vibe)","score":<0-10>,"comment":"..."}}
  ]
}}

Testo:
<<<
{text}
>>>
"""

def moderate(text: str):
    m = client.moderations.create(model="omni-moderation-latest", input=text)
    if m.results[0].flagged:
        raise HTTPException(400, "Testo non consentito dalla policy.")

def call_model(artist: str, text: str) -> Dict[str, Any]:
    prompt = PROMPT.format(artist=artist.strip()[:60], text=text.strip()[:8000], style_notice=STYLE_NOTICE)
    res = client.chat.completions.create(
        model="gpt-4o-mini",
        response_format={"type": "json_object"},
        temperature=0.4,
        messages=[
            {"role":"system","content":"Sei un motore di valutazione testi. Rispondi SOLO JSON valido."},
            {"role":"user","content": prompt}
        ]
    )
    return json.loads(res.choices[0].message.content)

def avg_score(data: Dict[str,Any]) -> float:
    return round(statistics.mean([max(0,min(10,float(j["score"]))) for j in data["judgments"]]), 2)

# ----- credits -----
FREE_CREDITS = {"solo": 1, "pvp": 1}

@app.post("/bootstrap")
def bootstrap(uid: str = Depends(get_uid)):
    ref = db.collection("users").document(uid)
    doc = ref.get()
    if doc.exists:
        return {"already": True}
    ref.set({
        "entitlement": "free",
        "credits": FREE_CREDITS,
        "createdAt": firestore.SERVER_TIMESTAMP
    })
    return {"ok": True, "credits": FREE_CREDITS}

def spend(uid: str, kind: str):
    def tx_job(tx):
        snap = tx.get(db.collection("users").document(uid))
        if not snap.exists:
            raise HTTPException(403, "Utente non inizializzato")
        data = snap.to_dict()
        if data.get("entitlement") == "plus":
            return
        credits = data.get("credits", {"solo":0,"pvp":0})
        if credits.get(kind,0) <= 0:
            raise HTTPException(402, "Crediti esauriti")
        credits[kind] -= 1
        tx.update(db.collection("users").document(uid), {"credits": credits})
    db.transaction()(tx_job)

# ----- endpoints -----
class JudgeResponse(BaseModel):
    artist: str
    judgments: list
    average: float

@app.post("/judge", response_model=JudgeResponse)
def judge(body: JudgeReq, uid: str = Depends(get_uid)):
    moderate(body.text)
    spend(uid, "solo")
    data = call_model(body.artist, body.text)
    return {"artist": body.artist, "judgments": data["judgments"], "average": avg_score(data)}

class BattleResponse(BaseModel):
    artist: str
    A: dict
    B: dict
    avgA: float
    avgB: float
    winner: str

@app.post("/battle", response_model=BattleResponse)
def battle(body: BattleReq, uid: str = Depends(get_uid)):
    moderate(body.text_a); moderate(body.text_b)
    spend(uid, "pvp")
    A = call_model(body.artist, body.text_a)
    B = call_model(body.artist, body.text_b)
    avgA, avgB = avg_score(A), avg_score(B)

    def getp(d,prefix):
        for j in d["judgments"]:
            if j["persona"].startswith(prefix): return float(j["score"])
        return 0.0

    winner = "draw"
    if avgA != avgB:
        winner = "A" if avgA > avgB else "B"
    else:
        for pref in ["Critico","Produttore","Psichiatra"]:
            sA, sB = getp(A,pref), getp(B,pref)
            if sA!=sB:
                winner = "A" if sA>sB else "B"; break

    # salva SOLO metadati, niente testi
    db.collection("battles").document().set({
        "participants": [uid],
        "avgA": avgA, "avgB": avgB, "winner": winner,
        "createdAt": firestore.SERVER_TIMESTAMP,
        "expiresAt": datetime.now(timezone.utc) + timedelta(hours=24)
    })
    # drop contenuti dalla RAM
    body.text_a = body.text_b = None
    return {"artist": body.artist, "A": A, "B": B, "avgA": avgA, "avgB": avgB, "winner": winner}

# (stub) verifica acquisti: da completare quando crei i prodotti abbonamento
@app.post("/billing/ack")
def billing_ack(payload: dict, uid: str = Depends(get_uid)):
    return {"ok": True, "entitlement": "plus"}