# 🎤 Selfjudge

[![Deploy to Render](https://render.com/images/deploy-to-render-button.svg)](https://render.com/deploy?repo=https://github.com/Marcone1983/selfjudge)

**AI-powered rap lyrics evaluation platform**

Selfjudge è una piattaforma che utilizza l'intelligenza artificiale per valutare testi rap/hip-hop da 5 prospettive diverse: psichiatra, produttore musicale, critico letterario, ascoltatore medio e il "vibe" di un artista specifico.

## 🏗️ Architettura

```
selfjudge/
├── backend/          # FastAPI server (Python)
│   ├── server.py     # Main API endpoints
│   ├── requirements.txt
│   ├── render.yaml   # Render.com deployment
│   └── README.md
└── android/          # Android app (Kotlin + Compose)
    └── app/          # Main app module
```

## 🚀 Features

### Backend API
- **POST /bootstrap** - Inizializzazione utente (1 credito solo + 1 pvp)
- **POST /judge** - Valutazione singola (consuma 1 credito "solo")
- **POST /battle** - Battaglia tra due testi (consuma 1 credito "pvp")
- **POST /billing/ack** - Gestione acquisti (stub)

### App Android
- 🔐 Autenticazione Firebase
- 🎯 Valutazione testi con AI
- ⚔️ Modalità battaglia
- 💰 Sistema crediti con paywall
- 📊 Dashboard punteggi

## 🔧 Setup Backend

### 1. Deploy su Render

1. Fai fork/clone di questo repo
2. Su Render: **Blueprints → New from repo**
3. Aggiungi environment variables:
   ```
   OPENAI_API_KEY=sk-...
   FIREBASE_PROJECT_ID=your-project-id
   FIREBASE_SERVICE_ACCOUNT_JSON={"type":"service_account",...}
   ```

### 2. Setup Firebase
- Crea progetto Firebase
- Attiva Authentication (Google/Email)
- Attiva Firestore Database
- Genera Service Account Key
- Configura TTL su campo `expiresAt` in Firestore

## 📱 Setup Android

### 1. Prerequisites
- Android Studio Koala+
- JDK 17+
- Android SDK 34+

### 2. Configuration
1. Aggiungi `google-services.json` in `android/app/`
2. Configura `local.properties`:
   ```
   api.base.url=https://your-api.onrender.com
   ```

### 3. Build & Run
```bash
cd android
./gradlew assembleDebug        # Debug APK
./gradlew bundleRelease        # Release AAB
```

## 🔐 Sicurezza

- ✅ Moderazione contenuti OpenAI
- ✅ Autenticazione Firebase obbligatoria
- ✅ Rate limiting (sistema crediti)
- ✅ Validazione input lato server
- ✅ Nessun salvataggio testi utente
- ✅ TTL automatico metadati battaglia (24h)

## 📊 AI Evaluation

Ogni testo viene valutato da 5 "giudici":

1. **Psichiatra** - Analisi emotiva e coerenza psicologica
2. **Produttore Musicale** - Flow, metrica, hook, orecchiabilità  
3. **Critico Letterario** - Stile, figure retoriche, qualità poetica
4. **Ascoltatore Medio** - Impatto immediato, chiarezza, memorabilità
5. **Artista (vibe)** - Coerenza con lo stile dell'artista scelto

Scala: 0-2 pessimo, 3-4 debole, 5-6 discreto, 7-8 buono, 9-10 eccellente.

## 🛠️ Tech Stack

**Backend:**
- FastAPI + Uvicorn
- OpenAI GPT-4o-mini
- Firebase Admin SDK
- Google Cloud Firestore

**Android:**
- Kotlin + Jetpack Compose
- Firebase Auth + Firestore
- Retrofit + OkHttp
- Material Design 3

**Deploy:**
- Backend: Render.com
- APK: GitHub Actions

## 📝 License

MIT License - vedi [LICENSE](LICENSE) per dettagli.

---

**Made with ❤️ and AI** 🤖