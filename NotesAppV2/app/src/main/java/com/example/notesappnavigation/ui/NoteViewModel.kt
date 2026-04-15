package com.example.notesappnavigation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesappnavigation.data.NoteRepository
import com.example.notesappnavigation.data.SettingsManager
import com.example.notesappnavigation.data.SortOrder
import com.example.notesappnavigation.model.Note
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class NotesUiState {
    object Loading : NotesUiState()
    data class Success(val notes: List<Note>) : NotesUiState()
    object Empty : NotesUiState()
    data class Error(val message: String) : NotesUiState()
}

class NoteViewModel(
    private val repository: NoteRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val isDarkTheme = settingsManager.isDarkTheme
    val sortOrder = settingsManager.sortOrder

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<NotesUiState> = combine(
        searchQuery,
        sortOrder
    ) { query, order ->
        query to order
    }.flatMapLatest { (query, order) ->
        if (query.isEmpty()) {
            repository.getAllNotes()
        } else {
            repository.searchNotes(query)
        }.map { notes ->
            val sortedNotes = when (order) {
                SortOrder.DATE_DESC -> notes.sortedByDescending { it.createdAt }
                SortOrder.DATE_ASC -> notes.sortedBy { it.createdAt }
                SortOrder.TITLE_ASC -> notes.sortedBy { it.title }
            }
            if (sortedNotes.isEmpty()) NotesUiState.Empty else NotesUiState.Success(sortedNotes)
        }
    }.onStart { emit(NotesUiState.Loading) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NotesUiState.Loading)

    val favoriteNotes: StateFlow<List<Note>> = repository.getAllNotes()
        .map { notes -> notes.filter { it.isFavorite } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun addNote(title: String, description: String, content: String, reminder: String) {
        viewModelScope.launch {
            repository.insertNote(Note(title = title, description = description, content = content, reminder = reminder))
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            repository.insertNote(note)
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            repository.deleteNote(id)
        }
    }

    fun toggleFavorite(note: Note) {
        viewModelScope.launch {
            repository.updateFavorite(note.id, !note.isFavorite)
        }
    }

    fun setDarkTheme(isDark: Boolean) {
        viewModelScope.launch {
            settingsManager.setDarkTheme(isDark)
        }
    }

    fun setSortOrder(order: SortOrder) {
        viewModelScope.launch {
            settingsManager.setSortOrder(order)
        }
    }
}
