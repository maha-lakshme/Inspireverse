package com.maha.inspireverse;

import android.app.Application;
import com.google.firebase.FirebaseApp

public class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
    }
}
