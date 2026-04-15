package com.example.notesappnavigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.notesappnavigation.components.MyBottomBar
import com.example.notesappnavigation.navigation.*
import com.example.notesappnavigation.screens.*
import com.example.notesappnavigation.ui.NoteViewModel
import com.example.notesappnavigation.ui.NotesUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: NoteViewModel) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val favoriteNotes by viewModel.favoriteNotes.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { MyBottomBar(navController) },
        floatingActionButton = {
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
            if (currentRoute == BottomNavItem.Notes.route) {
                FloatingActionButton(
                    onClick = { navController.navigate(Screen.AddNote.route) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Notes.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(BottomNavItem.Notes.route) {
                NoteListScreen(
                    uiState = uiState,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
                    onNoteClick = { id -> navController.navigate(Screen.NoteDetail.createRoute(id)) },
                    onFavClick = { note -> viewModel.toggleFavorite(note) }
                )
            }
            
            composable(BottomNavItem.Favorites.route) { 
                FavoritesScreen(
                    favoriteNotes = favoriteNotes,
                    onNoteClick = { id -> navController.navigate(Screen.NoteDetail.createRoute(id)) }
                )
            }
            
            composable(BottomNavItem.Profile.route) { 
                ProfileScreen(viewModel = viewModel) 
            }

            composable(Screen.AddNote.route) {
                AddNoteScreen(
                    onSave = { title, desc, content, reminder ->
                        viewModel.addNote(title, desc, content, reminder)
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                Screen.NoteDetail.route,
                arguments = listOf(navArgument("noteId") { type = NavType.LongType })
            ) { entry ->
                val id = entry.arguments?.getLong("noteId") ?: 0L
                // In a real app, we'd get this from the VM/Repo. 
                // For simplicity, we can find it in the current Success state if available
                val note = (uiState as? NotesUiState.Success)?.notes?.find { it.id == id }
                    ?: favoriteNotes.find { it.id == id }
                
                if (note != null) {
                    NoteDetailScreen(
                        note = note, 
                        onEditClick = { navController.navigate(Screen.EditNote.createRoute(id)) }, 
                        onDeleteClick = {
                            viewModel.deleteNote(id)
                            navController.popBackStack()
                        },
                        onBack = { navController.popBackStack() }
                    )
                }
            }

            composable(
                Screen.EditNote.route,
                arguments = listOf(navArgument("noteId") { type = NavType.LongType })
            ) { entry ->
                val id = entry.arguments?.getLong("noteId") ?: 0L
                val note = (uiState as? NotesUiState.Success)?.notes?.find { it.id == id }
                    ?: favoriteNotes.find { it.id == id }
                
                if (note != null) {
                    EditNoteScreen(
                        note = note,
                        onSave = { updatedNote ->
                            viewModel.updateNote(updatedNote)
                            navController.popBackStack()
                        },
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
