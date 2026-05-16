package com.example.notesappnavigation.database

import io.mockk.*
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class NoteRepositoryTest {
    private val database: NoteDatabase = mockk()
    private val queries: NoteEntityQueries = mockk(relaxed = true)
    private lateinit var repository: NoteRepository

    @BeforeTest
    fun setup() {
        every { database.noteEntityQueries } returns queries
        repository = NoteRepositoryImpl(database)
    }

    @Test
    fun `insertNote should call queries insertNote`() = runBlocking {
        // Arrange
        val title = "Test Title"
        val desc = "Test Desc"
        val content = "Test Content"
        val reminder = "2023-10-10"

        // Act
        repository.insertNote(title, desc, content, reminder)

        // Assert
        verify {
            queries.insertNote(
                id = null,
                title = title,
                description = desc,
                content = content,
                reminder = reminder,
                isFavorite = 0L,
                any() // createdAt
            )
        }
    }

    @Test
    fun `deleteNote should call queries deleteNote`() = runBlocking {
        // Arrange
        val noteId = 1L

        // Act
        repository.deleteNote(noteId)

        // Assert
        verify { queries.deleteNote(noteId) }
    }

    @Test
    fun `updateNote should call queries updateNote`() = runBlocking {
        // Arrange
        val id = 1L
        val title = "Updated Title"
        val desc = "Updated Desc"
        val content = "Updated Content"
        val reminder = "2023-11-11"

        // Act
        repository.updateNote(id, title, desc, content, reminder)

        // Assert
        verify { queries.updateNote(title, desc, content, reminder, id) }
    }

    @Test
    fun `updateFavorite should call queries updateFavorite`() = runBlocking {
        // Arrange
        val id = 1L
        val isFavorite = true

        // Act
        repository.updateFavorite(id, isFavorite)

        // Assert
        verify { queries.updateFavorite(1L, id) }
    }

    @Test
    fun `getNoteById should return note from queries`() {
        // Arrange
        val id = 1L
        val mockNote = NoteEntity(id, "Title", "Desc", "Content", "Reminder", 0L, 12345L)
        val mockQuery = mockk<app.cash.sqldelight.Query<NoteEntity>>()
        every { queries.selectById(id) } returns mockQuery
        every { mockQuery.executeAsOneOrNull() } returns mockNote

        // Act
        val result = repository.getNoteById(id)

        // Assert
        assertNotNull(result)
        assertEquals("Title", result.title)
        verify { queries.selectById(id) }
    }

    @Test
    fun `getAllNotes should call selectAll query`() = runBlocking {
        // Arrange
        val mockQuery = mockk<app.cash.sqldelight.Query<NoteEntity>>(relaxed = true)
        every { queries.selectAll() } returns mockQuery

        // Act: We just call it, mapping occurs in implementation
        repository.getAllNotes()

        // Assert
        verify { queries.selectAll() }
    }

    @Test
    fun `getFavoriteNotes should call selectFavorites query`() = runBlocking {
        // Arrange
        val mockQuery = mockk<app.cash.sqldelight.Query<NoteEntity>>(relaxed = true)
        every { queries.selectFavorites() } returns mockQuery

        // Act
        repository.getFavoriteNotes()

        // Assert
        verify { queries.selectFavorites() }
    }

    @Test
    fun `searchNotes should call search query with correct parameters`() = runBlocking {
        // Arrange
        val search = "pink"
        val mockQuery = mockk<app.cash.sqldelight.Query<NoteEntity>>(relaxed = true)
        every { queries.searchNotes(search, search) } returns mockQuery

        // Act
        repository.searchNotes(search)

        // Assert
        verify { queries.searchNotes(search, search) }
    }
}
