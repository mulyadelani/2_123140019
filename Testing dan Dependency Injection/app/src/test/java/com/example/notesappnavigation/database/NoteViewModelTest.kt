package com.example.notesappnavigation.database

import app.cash.turbine.test
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class NoteViewModelTest {
    private val repository: NoteRepository = mockk(relaxed = true)
    private lateinit var viewModel: NoteViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        // Mock repository.getAllNotes() to avoid null issues during initialization
        every { repository.getAllNotes() } returns flowOf(emptyList())
        every { repository.getFavoriteNotes() } returns flowOf(emptyList())
        viewModel = NoteViewModel(repository)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `notes flow should emit success state when repository returns data`() = runTest {
        // Arrange: Prepare mock data and repository behavior
        val mockNotes = listOf(
            NoteEntity(1L, "Title 1", "Desc 1", "Content 1", "Rem 1", 0L, 12345L)
        )
        every { repository.getAllNotes() } returns flowOf(mockNotes)

        // Act & Assert: Use Turbine to observe the StateFlow
        viewModel.notes.test {
            // Wait for initial state (empty list from stateIn)
            assertEquals(emptyList(), awaitItem())
            
            // Advance time to trigger debounce(300L) in ViewModel
            advanceTimeBy(400L)
            
            // Assert that the list now contains our mock note
            val result = awaitItem()
            assertEquals(1, result.size)
            assertEquals("Title 1", result[0].title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `isLoading should update during note fetching`() = runTest {
        // Arrange: Setup mock repository with a flow that doesn't emit immediately to capture loading state
        val repositoryFlow = MutableSharedFlow<List<NoteEntity>>()
        every { repository.searchNotes(any()) } returns repositoryFlow

        // Act & Assert: Verify the loading state transitions
        // We MUST collect 'notes' to trigger the flow logic since it uses WhileSubscribed
        val collectJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.notes.collect()
        }

        viewModel.isLoading.test {
            assertEquals(false, awaitItem()) // Initial state
            
            // Trigger a search update
            viewModel.updateSearchQuery("test")
            advanceTimeBy(400L) // Wait for debounce
            
            // Should emit true when flatMapLatest starts
            assertEquals(true, awaitItem())
            
            // Emit data from repository
            repositoryFlow.emit(emptyList<NoteEntity>())
            
            // Should emit false after onEach completes
            assertEquals(false, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `insertNote should call repository insert`() = runTest {
        // Arrange
        val title = "New Note"
        
        // Act
        viewModel.insertNote(title, "Desc", "Content", "Reminder")
        advanceUntilIdle()

        // Assert
        coVerify { repository.insertNote(title, any(), any(), any()) }
    }

    @Test
    fun `deleteNote should call repository delete`() = runTest {
        // Arrange
        val id = 123L

        // Act
        viewModel.deleteNote(id)
        advanceUntilIdle()

        // Assert
        coVerify { repository.deleteNote(id) }
    }

    @Test
    fun `favoriteNotes flow should emit data from repository`() = runTest {
        // Arrange: Prepare mock data for favorite notes
        val mockFavs = listOf(
            NoteEntity(2L, "Favorite Note", "Desc", "Content", "Rem", 1L, 12345L)
        )
        every { repository.getFavoriteNotes() } returns flowOf(mockFavs)
        val testViewModel = NoteViewModel(repository)

        // Act & Assert: Test flow using Turbine (Flow Test #3)
        testViewModel.favoriteNotes.test {
            // StateFlow initially emits the initialValue from stateIn
            assertEquals(emptyList(), awaitItem())
            // Then it emits the data from the repository flow
            assertEquals(mockFavs, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggleFavorite should call repository with negated value`() = runTest {
        // Arrange
        val id = 1L
        
        // Act: Toggle from false to true
        viewModel.toggleFavorite(id, currentIsFavorite = false)
        advanceUntilIdle()

        // Assert
        coVerify { repository.updateFavorite(id, true) }
    }

    @Test
    fun `updateNote should call repository update`() = runTest {
        // Act
        viewModel.updateNote(1L, "New Title", "New Desc", "New Content", "New Rem")
        advanceUntilIdle()

        // Assert
        coVerify { repository.updateNote(1L, "New Title", "New Desc", "New Content", "New Rem") }
    }
}
