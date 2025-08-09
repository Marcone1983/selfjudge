# Selfjudge Android App

App Android per la piattaforma Selfjudge - valutazione testi rap con AI.

## 🛠️ Setup

### 1. Prerequisites
- Android Studio Koala+ (2024.1.1+)
- JDK 17+
- Android SDK 34+

### 2. Firebase Setup
1. Crea un progetto Firebase
2. Aggiungi app Android con package `com.selfjudge`
3. Scarica `google-services.json` e mettilo in `app/`
4. Attiva Authentication (Email/Password + Google)
5. Attiva Firestore Database

### 3. Configuration
1. Copia `local.properties.example` in `local.properties`
2. Configura l'URL dell'API:
   ```
   api.base.url=https://your-api.onrender.com
   ```

### 4. Build & Run
```bash
# Debug APK
./gradlew assembleDebug

# Release AAB per Play Store
./gradlew bundleRelease

# Installa su device connesso
./gradlew installDebug
```

## 🏗️ Architettura

```
app/src/main/java/com/selfjudge/
├── data/
│   ├── api/           # Retrofit API client
│   ├── model/         # Data models
│   └── repository/    # Data repository
├── ui/
│   ├── screens/       # Compose screens
│   ├── components/    # UI components
│   └── theme/         # Material Design theme
└── utils/             # Utility classes
```

## 🎨 Features

- ✅ **Autenticazione Firebase** - Email/password
- ✅ **Valutazione singola** - Giudizio AI su testi rap
- ✅ **Modalità battaglia** - Confronto tra due testi
- ✅ **Sistema crediti** - Limitazioni uso gratuito
- ✅ **Material Design 3** - UI moderna e accessibile
- ✅ **Gestione stati** - Loading, errori, risultati
- 🔄 **Paywall** - Acquisto crediti (stub implementato)

## 🔧 Configurazione GitHub Actions

Il workflow automatico costruisce:
- **Debug APK** per test (ogni push)
- **Release AAB** per Play Store (ogni release)

Artifacts disponibili nei workflow runs.

## 📱 Schermate

1. **Auth** - Login/registrazione
2. **Judge** - Valutazione singola testo
3. **Battle** - Battaglia tra due testi  
4. **Profile** - Profilo utente e crediti

## 🚀 Deploy

### Debug APK
- Build automatico via GitHub Actions
- Download da Artifacts del workflow

### Release Play Store
1. Configura signing key in `app/build.gradle`
2. Build AAB: `./gradlew bundleRelease`
3. Upload su Play Console

---

**Tech Stack:** Kotlin, Jetpack Compose, Firebase, Retrofit, Material Design 3