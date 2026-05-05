package com.rapi.pocketwise

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import com.rapi.pocketwise.data.remote.GeminiService
import com.rapi.pocketwise.data.repository.NoteRepositoryImpl
import com.rapi.pocketwise.presentation.PocketWiseScreen
import com.rapi.pocketwise.presentation.PocketWiseViewModel

@Composable
fun App(
    geminiApiKey: String,
    batteryLevel: Int = 0,
    isCharging: Boolean = false,
    isOnline: Boolean = false,
    model: String = "",
    manufacturer: String = "",
    osVersion: String = ""
) {
    val viewModel = remember {
        val geminiService = GeminiService(
            apiKey = geminiApiKey
        )

        val noteRepository = NoteRepositoryImpl(
            geminiService = geminiService
        )

        PocketWiseViewModel(
            repository = noteRepository
        )
    }

    LaunchedEffect(batteryLevel, isCharging, isOnline, model, manufacturer, osVersion) {
        val currentState = viewModel.uiState.value
        // Hanya update jika ada perubahan nilai untuk mencegah reset state yang tidak perlu
        if (currentState.batteryLevel != batteryLevel || 
            currentState.isCharging != isCharging || 
            currentState.isOnline != isOnline) {
            
            println("APP_DEBUG: Memperbarui status perangkat")
            viewModel.updateDeviceStatus(
                batteryLevel = batteryLevel,
                isCharging = isCharging,
                isOnline = isOnline,
                model = model,
                manufacturer = manufacturer,
                osVersion = osVersion
            )
        }
    }

    MaterialTheme {
        PocketWiseScreen(
            viewModel = viewModel
        )
    }
}
