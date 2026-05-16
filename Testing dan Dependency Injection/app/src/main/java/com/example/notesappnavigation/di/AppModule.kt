package com.example.notesappnavigation.di

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.notesappnavigation.database.*
import com.example.notesappnavigation.platform.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

// Data Module: Singleton for Database and Repository
val dataModule = module {
    single {
        AndroidSqliteDriver(NoteDatabase.Schema, androidContext(), "notes_v2.db")
    }
    single { NoteDatabase(get()) }
    single<NoteRepository> { NoteRepositoryImpl(get()) }
    single { SettingsDataStore(androidContext()) }
}

// ViewModel Module: Injecting NoteViewModel
val viewModelModule = module {
    viewModel { NoteViewModel(get()) }
}

// Platform Module: Platform-specific implementations
val platformModule = module {
    single<DeviceInfo> { AndroidDeviceInfo() }
    single<NetworkMonitor> { AndroidNetworkMonitor(androidContext()) }
    single<BatteryInfo> { AndroidBatteryInfo(androidContext()) }
}

// Global appModule to combine all modules
val appModule = listOf(dataModule, viewModelModule, platformModule)
