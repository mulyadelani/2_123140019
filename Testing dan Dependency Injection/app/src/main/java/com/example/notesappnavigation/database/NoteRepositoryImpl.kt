package com.example.notesappnavigation.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(
    private val database: NoteDatabase
) : NoteRepository {
    private val queries = database.noteEntityQueries

    override fun getAllNotes(): Flow<List<NoteEntity>> =
        queries.selectAll().asFlow().mapToList(Dispatchers.IO)

    override fun getFavoriteNotes(): Flow<List<NoteEntity>> =
        queries.selectFavorites().asFlow().mapToList(Dispatchers.IO)

    override fun searchNotes(query: String): Flow<List<NoteEntity>> =
        queries.searchNotes(query, query).asFlow().mapToList(Dispatchers.IO)

    override suspend fun insertNote(title: String, description: String, content: String, reminder: String) {
        queries.insertNote(
            id = null,
            title = title,
            description = description,
            content = content,
            reminder = reminder,
            isFavorite = 0L,
            createdAt = System.currentTimeMillis()
        )
    }

    override suspend fun updateNote(id: Long, title: String, description: String, content: String, reminder: String) {
        queries.updateNote(title, description, content, reminder, id)
    }

    override suspend fun updateFavorite(id: Long, isFavorite: Boolean) {
        queries.updateFavorite(if (isFavorite) 1L else 0L, id)
    }

    override suspend fun deleteNote(id: Long) {
        queries.deleteNote(id)
    }

    override fun getNoteById(id: Long): NoteEntity? {
        return queries.selectById(id).executeAsOneOrNull()
    }
}
