package com.rapi.pocketwise.presentation

import com.rapi.pocketwise.data.model.Note
import com.rapi.pocketwise.data.repository.NoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PocketWiseViewModel(
    private val repository: NoteRepository
) {
    init {
        println("PocketWiseViewModel created!")
    }

    private val _uiState = MutableStateFlow(PocketWiseUiState())
    val uiState: StateFlow<PocketWiseUiState> = _uiState.asStateFlow()

    private val viewModelScope = CoroutineScope(Dispatchers.Main)

    fun onTitleChanged(value: String) {
        println("Title changed to: $value")
        _uiState.update { it.copy(noteTitleInput = value) }
    }

    fun onDescriptionChanged(value: String) {
        _uiState.update { it.copy(noteDescriptionInput = value) }
    }

    fun onReminderChanged(value: String) {
        _uiState.update { it.copy(noteReminderInput = value) }
    }

    fun onContentChanged(value: String) {
        _uiState.update { it.copy(noteContentInput = value) }
    }

    fun toggleFavorite(noteId: String) {
        _uiState.update { currentState ->
            val updatedNotes = currentState.notes.map { note ->
                if (note.id == noteId) note.copy(isFavorite = !note.isFavorite) else note
            }
            currentState.copy(notes = updatedNotes)
        }
    }

    fun saveNote() {
        // Ambil data SEBELUM update untuk log
        val inputTitle = _uiState.value.noteTitleInput
        val inputDesc = _uiState.value.noteDescriptionInput
        println("--- PROSES_SIMPAN_MULAI: Judul='$inputTitle' ---")

        if (inputTitle.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Judul tidak boleh kosong") }
            return
        }

        _uiState.update { state ->
            val noteId = state.editingNoteId ?: (state.notes.size + 1).toString()
            val newNote = Note(
                id = noteId,
                title = state.noteTitleInput,
                description = state.noteDescriptionInput,
                reminder = state.noteReminderInput,
                content = state.noteContentInput,
                date = "10 Mei 2024",
                isFavorite = false
            )

            // Pastikan kita menambahkannya ke list yang ada
            val updatedNotes = (state.notes + newNote).distinctBy { it.id }
            
            println("--- STATE_DIUPDATE: NoteID=$noteId, ListSize=${updatedNotes.size} ---")
            
            state.copy(
                notes = updatedNotes,
                noteTitleInput = "",
                noteDescriptionInput = "",
                noteReminderInput = "",
                noteContentInput = "",
                editingNoteId = null
            )
        }
        
        println("--- PROSES_SIMPAN_SELESAI: CurrentSize=${_uiState.value.notes.size} ---")
    }

    fun deleteNote(noteId: String) {
        _uiState.update { state ->
            state.copy(notes = state.notes.filter { it.id != noteId })
        }
    }

    fun editNote(note: Note) {
        _uiState.update {
            it.copy(
                noteTitleInput = note.title,
                noteDescriptionInput = note.description,
                noteReminderInput = note.reminder,
                noteContentInput = note.content,
                editingNoteId = note.id
            )
        }
    }

    fun translateAI() {
        val textToTranslate = _uiState.value.noteContentInput
        println("--- TRANSLATE_AI_MULAI: Teks='$textToTranslate' ---")
        
        if (textToTranslate.isBlank()) {
            println("--- TRANSLATE_AI_GAGAL: Teks Kosong ---")
            _uiState.update { it.copy(errorMessage = "Isi catatan kosong, tidak ada yang bisa diterjemahkan.") }
            return
        }

        _uiState.update { it.copy(isAiLoading = true, translatedText = "") }

        viewModelScope.launch {
            println("--- TRANSLATE_AI_CALLING_REPO ---")
            repository.translateText(textToTranslate)
                .onSuccess { translation ->
                    println("--- TRANSLATE_AI_BERHASIL: '$translation' ---")
                    _uiState.update { it.copy(isAiLoading = false, translatedText = translation) }
                }
                .onFailure { error ->
                    println("--- TRANSLATE_AI_ERROR: ${error.message} ---")
                    _uiState.update { it.copy(isAiLoading = false, errorMessage = "Gagal menerjemahkan: ${error.message}") }
                }
        }
    }

    fun updateDeviceStatus(
        batteryLevel: Int,
        isCharging: Boolean,
        isOnline: Boolean,
        model: String,
        manufacturer: String,
        osVersion: String
    ) {
        _uiState.update {
            it.copy(
                batteryLevel = batteryLevel,
                isCharging = isCharging,
                isOnline = isOnline,
                deviceModel = model,
                deviceManufacturer = manufacturer,
                osVersion = osVersion
            )
        }
    }

    fun setDarkMode(enabled: Boolean) {
        _uiState.update { it.copy(isDarkMode = enabled) }
    }

    fun setSortOrder(newestFirst: Boolean) {
        _uiState.update { it.copy(sortNewestFirst = newestFirst) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
