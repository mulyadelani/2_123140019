package com.example.notesappnavigation.database

import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<NoteEntity>>
    fun getFavoriteNotes(): Flow<List<NoteEntity>>
    fun searchNotes(query: String): Flow<List<NoteEntity>>
    suspend fun insertNote(title: String, description: String, content: String, reminder: String)
    suspend fun updateNote(id: Long, title: String, description: String, content: String, reminder: String)
    suspend fun updateFavorite(id: Long, isFavorite: Boolean)
    suspend fun deleteNote(id: Long)
    fun getNoteById(id: Long): NoteEntity?
}
