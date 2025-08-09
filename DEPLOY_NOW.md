# 🚀 DEPLOY IMMEDIATO - SELFJUDGE

## ⚡ DEPLOY BACKEND IN 2 MINUTI

### 1️⃣ **Apri Render.com**
👉 https://render.com

### 2️⃣ **Clicca "New +" → "Blueprint"**

### 3️⃣ **Connetti GitHub Repository**
- Seleziona: `Marcone1983/selfjudge`
- Render trova automaticamente `backend/render.yaml`

### 4️⃣ **Aggiungi Environment Variables**

Clicca su "Advanced" e aggiungi:

#### **OPENAI_API_KEY** (OBBLIGATORIO)
```
sk-proj-... (la tua chiave OpenAI)
```
⚠️ **IMPORTANTE**: Crea una NUOVA chiave su https://platform.openai.com/api-keys

#### **FIREBASE_PROJECT_ID** (OBBLIGATORIO)
```
selfjudge-app
```
(O il tuo project ID da Firebase Console)

#### **FIREBASE_SERVICE_ACCOUNT_JSON** (OBBLIGATORIO)
```
{"type":"service_account","project_id":"...TUTTO IL JSON..."}
```
📥 Scarica da: Firebase Console → Project Settings → Service Accounts → Generate New Private Key

### 5️⃣ **Deploy!**
- Clicca **"Apply"**
- Attendi 2-3 minuti
- URL finale: `https://selfjudge-api.onrender.com`

---

## ✅ VERIFICA DEPLOY

### Test API Online:
```bash
curl https://selfjudge-api.onrender.com/health
```

Risposta attesa:
```json
{"status":"healthy"}
```

### Test Completo:
```bash
curl https://selfjudge-api.onrender.com/
```

Risposta attesa:
```json
{
  "status": "online",
  "service": "Selfjudge API",
  "version": "1.0.0",
  "endpoints": ["/bootstrap", "/judge", "/battle"]
}
```

---

## 📱 APP ANDROID

Una volta che il backend è online:

1. **Scarica APK**: https://github.com/Marcone1983/selfjudge/actions
2. **Installa** su dispositivo Android
3. **Registrati** con email/password
4. **Usa l'app!**

---

## ⚠️ TROUBLESHOOTING

### "OPENAI_API_KEY mancante"
→ Aggiungi la chiave nelle Environment Variables di Render

### "FIREBASE_SERVICE_ACCOUNT_JSON mancante"  
→ Aggiungi il JSON completo (copia-incolla tutto)

### "502 Bad Gateway"
→ Il server sta ancora avviandosi, attendi 30 secondi

### "Application failed to respond"
→ Controlla i logs in Render Dashboard

---

## 🎯 FATTO!

Backend online in 2 minuti su Render.com con piano **FREE**! 🎉