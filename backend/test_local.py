#!/usr/bin/env python3
"""
Test script per verificare il backend localmente prima del deploy Render
"""
import os
import json
from dotenv import load_dotenv

# Carica environment variables
load_dotenv()

print("🔍 Checking environment variables...")

# Check OpenAI
openai_key = os.getenv("OPENAI_API_KEY")
if openai_key and openai_key.startswith("sk-"):
    print("✅ OPENAI_API_KEY: Configured")
else:
    print("❌ OPENAI_API_KEY: Missing or invalid")

# Check Firebase Project ID  
firebase_project = os.getenv("FIREBASE_PROJECT_ID")
if firebase_project:
    print(f"✅ FIREBASE_PROJECT_ID: {firebase_project}")
else:
    print("❌ FIREBASE_PROJECT_ID: Missing")

# Check Firebase Service Account
firebase_json = os.getenv("FIREBASE_SERVICE_ACCOUNT_JSON")
if firebase_json:
    try:
        data = json.loads(firebase_json)
        if "project_id" in data and "private_key" in data:
            print(f"✅ FIREBASE_SERVICE_ACCOUNT_JSON: Valid (project: {data['project_id']})")
        else:
            print("❌ FIREBASE_SERVICE_ACCOUNT_JSON: Invalid structure")
    except:
        print("❌ FIREBASE_SERVICE_ACCOUNT_JSON: Invalid JSON")
else:
    print("❌ FIREBASE_SERVICE_ACCOUNT_JSON: Missing")

print("\n📝 To run the server locally:")
print("   uvicorn server:app --reload --port 8000")
print("\n🚀 To deploy on Render:")
print("   1. Push this code to GitHub")
print("   2. Connect GitHub repo to Render")
print("   3. Render will use render.yaml automatically")
print("   4. Add the environment variables in Render dashboard")