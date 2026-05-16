package com.example.notesappnavigation

import android.app.Application

class NotesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Koin using the function in App.kt
        initKoin()
    }
}
