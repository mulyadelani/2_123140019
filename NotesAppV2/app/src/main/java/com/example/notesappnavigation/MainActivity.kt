package com.example.notesappnavigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notesappnavigation.data.NoteRepository
import com.example.notesappnavigation.data.SettingsManager
import com.example.notesappnavigation.database.DatabaseDriverFactory
import com.example.notesappnavigation.database.NoteDatabase
import com.example.notesappnavigation.ui.NoteViewModel
import com.example.notesappnavigation.ui.NoteViewModelFactory
import com.example.notesappnavigation.ui.theme.NotesAppNavigationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val driver = DatabaseDriverFactory(this).createDriver()
        val database = NoteDatabase(driver)
        val repository = NoteRepository(database)
        val settingsManager = SettingsManager(this)
        
        setContent {
            val viewModel: NoteViewModel = viewModel(
                factory = NoteViewModelFactory(repository, settingsManager)
            )
            val isDarkTheme by viewModel.isDarkTheme.collectAsState(initial = false)

            NotesAppNavigationTheme(darkTheme = isDarkTheme ?: false) {
                MainScreen(viewModel)
            }
        }
    }
}
