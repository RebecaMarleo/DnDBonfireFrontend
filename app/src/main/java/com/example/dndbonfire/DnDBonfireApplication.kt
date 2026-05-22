package com.example.dndbonfire

import android.app.Application
import com.google.firebase.FirebaseApp

class DnDBonfireApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}