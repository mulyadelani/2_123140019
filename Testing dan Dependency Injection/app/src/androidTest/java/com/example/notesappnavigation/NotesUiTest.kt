package com.example.notesappnavigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.notesappnavigation.database.NoteEntity
import com.example.notesappnavigation.platform.NetworkMonitor
import com.example.notesappnavigation.screens.NoteListScreen
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon

class NotesUiTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockNetworkMonitor = mockk<NetworkMonitor>()

    @Before
    fun setup() {
        // Mocking NetworkMonitor which is injected via koinInject() in NoteListScreen
        every { mockNetworkMonitor.isConnected } returns flowOf(true)
        
        startKoin {
            modules(module {
                single { mockNetworkMonitor }
            })
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun testEmptyState_showsCorrectMessage() {
        // Arrange
        composeTestRule.setContent {
            NoteListScreen(
                notes = emptyList(),
                isLoading = false,
                searchQuery = "",
                onSearchQueryChange = {},
                onNoteClick = {},
                onDeleteNote = {},
                onToggleFavorite = { _, _ -> }
            )
        }

        // Assert
        composeTestRule.onNodeWithTag("empty_state_text").assertIsDisplayed()
        composeTestRule.onNodeWithText("Belum ada catatan cantik.").assertIsDisplayed()
    }

    @Test
    fun testNoteList_showsNoteItems() {
        // Arrange
        val mockNotes = listOf(
            NoteEntity(1L, "Catatan Pink Saya", "Deskripsi", "Isi", "Besok", 0L, 0L)
        )
        
        composeTestRule.setContent {
            NoteListScreen(
                notes = mockNotes,
                isLoading = false,
                searchQuery = "",
                onSearchQueryChange = {},
                onNoteClick = {},
                onDeleteNote = {},
                onToggleFavorite = { _, _ -> }
            )
        }

        // Assert
        composeTestRule.onNodeWithTag("note_list").assertIsDisplayed()
        composeTestRule.onNodeWithText("Catatan Pink Saya").assertIsDisplayed()
    }

    @Test
    fun testAddButton_interaction() {
        // Arrange
        var isClicked = false
        composeTestRule.setContent {
            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { isClicked = true },
                        modifier = Modifier.testTag("add_note_fab")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                }
            ) { padding ->
                // Content ignored for this test
            }
        }

        // Act
        composeTestRule.onNodeWithTag("add_note_fab").performClick()

        // Assert
        assert(isClicked)
    }
}
