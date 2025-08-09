package com.selfjudge

import android.app.Application
import com.google.firebase.FirebaseApp

class SelfJudgeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}