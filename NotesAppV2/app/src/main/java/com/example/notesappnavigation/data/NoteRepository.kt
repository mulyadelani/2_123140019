package com.example.notesappnavigation.data

import com.example.notesappnavigation.database.NoteDatabase
import com.example.notesappnavigation.model.Note
import com.example.notesappnavigation.database.NoteEntity
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepository(private val db: NoteDatabase) {
    private val queries = db.noteEntityQueries

    fun getAllNotes(): Flow<List<Note>> {
        return queries.getAllNotes()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { entities -> entities.map { it.toNote() } }
    }

    fun searchNotes(query: String): Flow<List<Note>> {
        return queries.searchNotes(query)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { entities -> entities.map { it.toNote() } }
    }

    suspend fun insertNote(note: Note) {
        queries.insertNote(
            id = if (note.id == 0L) null else note.id,
            title = note.title,
            description = note.description,
            content = note.content,
            reminder = note.reminder,
            isFavorite = note.isFavorite,
            createdAt = note.createdAt
        )
    }

    suspend fun deleteNote(id: Long) {
        queries.deleteNote(id)
    }

    suspend fun updateFavorite(id: Long, isFavorite: Boolean) {
        queries.updateFavorite(isFavorite, id)
    }

    private fun NoteEntity.toNote(): Note {
        return Note(
            id = id,
            title = title,
            description = description,
            content = content,
            reminder = reminder,
            isFavorite = isFavorite,
            createdAt = createdAt
        )
    }
}
