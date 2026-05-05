package com.rapi.pocketwise.presentation

import com.rapi.pocketwise.data.model.Note

data class PocketWiseUiState(
    // Note editing
    val noteTitleInput: String = "",
    val noteDescriptionInput: String = "",
    val noteReminderInput: String = "",
    val noteContentInput: String = "",
    val editingNoteId: String? = null,

    // List of notes
    val notes: List<Note> = emptyList(),
    
    // AI Translation
    val translatedText: String = "",
    val isAiLoading: Boolean = false,
    
    // UI state
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    
    // Settings & Device info
    val isDarkMode: Boolean = false,
    val sortNewestFirst: Boolean = true,
    val batteryLevel: Int = 0,
    val isCharging: Boolean = false,
    val isOnline: Boolean = false,
    val deviceModel: String = "",
    val deviceManufacturer: String = "",
    val osVersion: String = ""
)
