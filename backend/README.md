# Selfjudge API (FastAPI)
- Endpoint: `/bootstrap` (assegna 1 credito solo + 1 pvp una sola volta)
- `/judge` (consuma 1 credito solo se non PLUS)
- `/battle` (consuma 1 credito pvp se non PLUS)
- Nessuna cronologia: i testi non vengono salvati; in Firestore salviamo solo metadati (winner, punteggi, TTL 24h).

## Env richieste
- `OPENAI_API_KEY`
- `FIREBASE_PROJECT_ID`
- `FIREBASE_SERVICE_ACCOUNT_JSON` (contenuto JSON intero)

## Avvio locale
```bash
pip install -r requirements.txt
export OPENAI_API_KEY=sk-...
export FIREBASE_PROJECT_ID=...
export FIREBASE_SERVICE_ACCOUNT_JSON='{"type":"service_account",...}'
uvicorn server:app --reload --port 8000