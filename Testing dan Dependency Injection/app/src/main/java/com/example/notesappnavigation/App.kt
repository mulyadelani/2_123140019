package com.example.notesappnavigation

import com.example.notesappnavigation.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Global Koin initialization function as requested.
 * In a real KMP project, this would be in commonMain.
 */
fun NotesApplication.initKoin() {
    startKoin {
        androidContext(this@initKoin)
        modules(appModule)
    }
}
