#!/usr/bin/env python3
"""
Script per guidare il deploy su Render
"""
import webbrowser
import time

print("""
🚀 DEPLOY SELFJUDGE BACKEND SU RENDER
=====================================

Questo script ti guiderà nel deploy del backend su Render.

📋 PREREQUISITI:
- Account Render.com (gratuito)
- Account GitHub con accesso al repo
- Chiavi API pronte (OpenAI, Firebase)

""")

input("Premi ENTER per continuare...")

print("""
STEP 1: APERTURA RENDER
-----------------------
""")
print("Sto aprendo Render.com nel browser...")
time.sleep(2)
webbrowser.open("https://dashboard.render.com/select-repo?type=web")

print("""
STEP 2: CONNESSIONE GITHUB
---------------------------
1. Se richiesto, fai login con il tuo account
2. Clicca su "Connect GitHub account" se non l'hai già fatto
3. Autorizza Render ad accedere ai tuoi repository
""")

input("Premi ENTER quando hai connesso GitHub...")

print("""
STEP 3: SELEZIONE REPOSITORY
-----------------------------
1. Cerca e seleziona: Marcone1983/selfjudge
2. Se non lo vedi, clicca "Configure account" per dare accesso
""")

input("Premi ENTER quando hai selezionato il repository...")

print("""
STEP 4: CONFIGURAZIONE SERVICE
-------------------------------
Compila i campi:

Name: selfjudge-api
Region: Frankfurt (EU) o Oregon (US)
Branch: main
Root Directory: backend
Runtime: Python 3
Build Command: pip install -r requirements.txt
Start Command: uvicorn server:app --host 0.0.0.0 --port $PORT
Instance Type: Free
""")

input("Premi ENTER quando hai configurato...")

print("""
STEP 5: ENVIRONMENT VARIABLES
------------------------------
Clicca su "Advanced" e aggiungi:

1. OPENAI_API_KEY
   Valore: sk-proj-... (la tua chiave)

2. FIREBASE_PROJECT_ID  
   Valore: selfjudge-app (o il tuo)

3. FIREBASE_SERVICE_ACCOUNT_JSON
   Valore: {"type":"service_account"... (tutto il JSON)
""")

input("Premi ENTER quando hai aggiunto le variabili...")

print("""
STEP 6: DEPLOY!
---------------
1. Clicca "Create Web Service"
2. Attendi 2-3 minuti per il primo deploy
3. L'URL sarà: https://selfjudge-api.onrender.com
""")

print("\n⏰ Attendo il deploy...")
time.sleep(5)

print("\nApertura del servizio per verificare lo stato...")
webbrowser.open("https://dashboard.render.com/web/new")

print("""
✅ VERIFICA DEPLOY
------------------
Una volta completato, testa l'API:
""")

print("\ncurl https://selfjudge-api.onrender.com/health")
print("\n🎉 Fatto! Il backend dovrebbe essere online!")