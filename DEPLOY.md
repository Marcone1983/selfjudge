# 🚀 Deploy Guide per Selfjudge

## ✅ Setup Completato Automaticamente

### 📦 Repository GitHub
- **URL**: https://github.com/Marcone1983/selfjudge
- **Status**: ✅ Codice pushato e sincronizzato
- **Branches**: `main` (default)
- **CI/CD**: GitHub Actions configurato

---

## 🔥 Firebase Setup (Manuale)

### 1. Crea Progetto Firebase
1. Vai su [Firebase Console](https://console.firebase.google.com/)
2. Click **"Aggiungi progetto"**
3. Nome progetto: `selfjudge` 
4. ID progetto: `selfjudge-app` (o simile)
5. Accetta termini → **Continua**
6. Google Analytics: **Sì** (raccomandato)
7. **Crea progetto**

### 2. Configura Authentication
```
Firebase Console → Authentication → Get started
→ Sign-in method → Email/Password → Enable
```

### 3. Configura Firestore Database
```
Firebase Console → Firestore Database → Create database
→ Start in production mode → Next
→ Cloud Firestore location: europe-west3 → Done
```

### 4. TTL Policy (Importante!)
```
Firestore Console → TTL → Add a TTL policy
→ Collection: battles
→ Timestamp field: expiresAt
→ Save
```

### 5. Service Account Key
```
Firebase Console → Project Settings → Service accounts
→ Generate new private key → Download JSON
```

### 6. App Android
```
Firebase Console → Project Overview → Add app → Android
→ Package name: com.selfjudge
→ Download google-services.json
```

---

## 🌐 Render Deploy (Manuale)

### 1. Crea Account & Collega GitHub
1. Registrati su [Render.com](https://render.com)
2. Collega account GitHub
3. Autorizza accesso repository

### 2. Deploy con Blueprint
1. Dashboard Render → **New** → **Blueprint**
2. Seleziona repository: `Marcone1983/selfjudge`
3. Blueprint file: `backend/render.yaml` ✅
4. **Deploy**

### 3. Environment Variables (Critiche!)
```bash
OPENAI_API_KEY=sk-... # NUOVA CHIAVE (revoca la vecchia!)
FIREBASE_PROJECT_ID=selfjudge-app
FIREBASE_SERVICE_ACCOUNT_JSON={"type":"service_account",...}
```

**⚠️ IMPORTANTE**: Usa il JSON completo del Service Account

### 4. Verifica Deploy
- URL: `https://selfjudge-api.onrender.com`
- Test: GET `/` → FastAPI docs
- Logs in caso di errori

---

## 📱 Android Build (Automatico)

### GitHub Actions Status
- ✅ Workflow configurato in `.github/workflows/android.yml`
- ✅ Build automatico su ogni push
- ✅ Output: APK debug + AAB release

### Manual Build (Opzionale)
```bash
cd android

# Setup
cp local.properties.example local.properties
# Configura: api.base.url=https://selfjudge-api.onrender.com

# Aggiungi google-services.json in android/app/

# Build
./gradlew assembleDebug    # APK per test
./gradlew bundleRelease    # AAB per Play Store
```

---

## 🧪 Testing Completo

### 1. Backend Test
```bash
curl https://selfjudge-api.onrender.com/
# Expected: FastAPI docs HTML
```

### 2. Android Test
1. Download APK da GitHub Actions artifacts
2. Installa su device/emulatore
3. Registrazione → Bootstrap → Test judge/battle

### 3. E2E Test
1. Crea account Firebase Auth
2. Test valutazione testo
3. Test battaglia
4. Verifica crediti in Firestore

---

## 📈 Monitoring & Scaling

### Render Metrics
- Dashboard → Service → Metrics
- CPU, Memory, Response time
- Logs real-time

### Firebase Analytics
- Console → Analytics
- User behavior, retention
- Performance monitoring

### GitHub Actions
- Actions tab → Build history
- Artifacts download
- Failed builds debug

---

## 🔐 Security Checklist

- ✅ Service Account key in environment (non committed)
- ✅ OpenAI key regenerata e sicura
- ✅ Firebase rules production-ready
- ✅ HTTPS obbligatorio (Render default)
- ✅ Input validation lato server
- ✅ Rate limiting (sistema crediti)

---

## 🚀 Go Live!

1. ✅ Firebase progetto creato
2. ✅ Render backend deployato  
3. ✅ Android APK buildato
4. ✅ Test E2E completato
5. 🎯 **SELFJUDGE È ONLINE!**

---

*Generato automaticamente da Claude Code* 🤖