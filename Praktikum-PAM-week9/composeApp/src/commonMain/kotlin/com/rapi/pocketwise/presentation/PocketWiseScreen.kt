package com.rapi.pocketwise.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rapi.pocketwise.data.model.Note

// Color Palette
val PinkPrimary = Color(0xFFFF80AB)
val PinkLight = Color(0xFFFFF5F8)
val PinkDark = Color(0xFFC51162)
val TextDark = Color(0xFF880E4F)
val GrayText = Color(0xFF757575)

@Composable
fun PocketWiseScreen(viewModel: PocketWiseViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    var currentScreen by remember { mutableStateOf("notes") }

    Scaffold(
        bottomBar = {
            BottomNavigation(currentScreen) { currentScreen = it }
        },
        floatingActionButton = {
            if (currentScreen != "edit") {
                FloatingActionButton(
                    onClick = { 
                        viewModel.editNote(Note("", "", "", "", "", isFavorite = false, date = ""))
                        currentScreen = "edit" 
                    },
                    containerColor = PinkPrimary,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Note")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PinkLight)
                .padding(bottom = padding.calculateBottomPadding())
        ) {
            HeaderSection()

            when (currentScreen) {
                "notes" -> NotesScreen(uiState, viewModel) { currentScreen = "edit" }
                "favorites" -> FavoritesScreen(uiState, viewModel) { currentScreen = "edit" }
                "profile" -> ProfileScreen()
                "settings" -> SettingsScreen(uiState, viewModel)
                "edit" -> EditNoteScreen(uiState, viewModel) { currentScreen = "notes" }
            }
        }
    }
}

@Composable
fun HeaderSection() {
    Surface(
        color = PinkPrimary,
        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
        modifier = Modifier.fillMaxWidth().height(100.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = "Pink Notes",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun BottomNavigation(currentScreen: String, onScreenSelected: (String) -> Unit) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentScreen == "notes",
            onClick = { onScreenSelected("notes") },
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Notes", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PinkPrimary,
                selectedTextColor = PinkPrimary,
                indicatorColor = PinkLight
            )
        )
        NavigationBarItem(
            selected = currentScreen == "favorites",
            onClick = { onScreenSelected("favorites") },
            icon = { Icon(Icons.Default.Favorite, null) },
            label = { Text("Favorites", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PinkPrimary,
                selectedTextColor = PinkPrimary,
                indicatorColor = PinkLight
            )
        )
        NavigationBarItem(
            selected = currentScreen == "profile",
            onClick = { onScreenSelected("profile") },
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("Profile", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PinkPrimary,
                selectedTextColor = PinkPrimary,
                indicatorColor = PinkLight
            )
        )
        NavigationBarItem(
            selected = currentScreen == "settings",
            onClick = { onScreenSelected("settings") },
            icon = { Icon(Icons.Default.Settings, null) },
            label = { Text("Settings", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PinkPrimary,
                selectedTextColor = PinkPrimary,
                indicatorColor = PinkLight
            )
        )
    }
}

@Composable
fun NotesScreen(uiState: PocketWiseUiState, viewModel: PocketWiseViewModel, onEdit: () -> Unit) {
    println("--- RENDER_HOME: Menampilkan ${uiState.notes.size} Catatan ---")

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ConnectivityBanner(uiState.isOnline)
        }

        item {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Cari catatan pink...", color = GrayText) },
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp)),
                leadingIcon = { Icon(Icons.Default.Search, null, tint = PinkPrimary) },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = PinkPrimary
                ),
                shape = RoundedCornerShape(24.dp)
            )
        }

        items(uiState.notes) { note ->
            NoteCard(note, onFavorite = { viewModel.toggleFavorite(note.id) }, onDelete = { viewModel.deleteNote(note.id) }, onEdit = {
                viewModel.editNote(note)
                onEdit()
            })
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
fun ConnectivityBanner(isOnline: Boolean) {
    val bgColor = if (isOnline) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
    val textColor = if (isOnline) Color(0xFF2E7D32) else Color(0xFFC62828)
    val text = if (isOnline) "Connected to Pink Network" else "Offline"
    val icon = if (isOnline) Icons.Default.CheckCircle else Icons.Default.Warning

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .background(bgColor, RoundedCornerShape(12.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(icon, null, tint = textColor, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(8.dp))
        Text(text, color = textColor, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun NoteCard(note: Note, onFavorite: () -> Unit, onDelete: () -> Unit, onEdit: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onEdit() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(note.title, color = TextDark, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(note.description, color = GrayText, fontSize = 14.sp)
            }
            IconButton(onClick = onFavorite) {
                Icon(
                    if (note.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    null,
                    tint = PinkPrimary
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, null, tint = Color(0xFFFF8A80))
            }
        }
    }
}

@Composable
fun FavoritesScreen(uiState: PocketWiseUiState, viewModel: PocketWiseViewModel, onEdit: () -> Unit) {
    val favoriteNotes = uiState.notes.filter { it.isFavorite }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                "💖 Favorite Notes 💖",
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                textAlign = TextAlign.Center,
                color = TextDark,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(favoriteNotes) { note ->
            NoteCard(note, onFavorite = { viewModel.toggleFavorite(note.id) }, onDelete = { viewModel.deleteNote(note.id) }, onEdit = {
                viewModel.editNote(note)
                onEdit()
            })
        }
    }
}

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "My Profile",
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            color = TextDark,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Box(contentAlignment = Alignment.BottomEnd) {
            Surface(
                modifier = Modifier.size(120.dp),
                shape = CircleShape,
                color = PinkPrimary.copy(alpha = 0.2f),
                border = androidx.compose.foundation.BorderStroke(2.dp, PinkPrimary)
            ) {
                Icon(
                    Icons.Default.Person,
                    null,
                    modifier = Modifier.padding(24.dp),
                    tint = PinkPrimary
                )
            }
            Surface(
                modifier = Modifier.size(32.dp).offset(x = (-4).dp, y = (-4).dp),
                shape = CircleShape,
                color = PinkPrimary,
                shadowElevation = 4.dp
            ) {
                Icon(Icons.Default.Edit, null, modifier = Modifier.padding(6.dp), tint = Color.White)
            }
        }

        Spacer(Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Mulya Delani", color = TextDark, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                Text("Pinky Developer 🌸", color = PinkPrimary, fontSize = 14.sp)
                
                Spacer(Modifier.height(24.dp))
                
                Surface(
                    color = PinkLight,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.Info, null, tint = PinkPrimary, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("NIM: 123140019", color = TextDark, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(uiState: PocketWiseUiState, viewModel: PocketWiseViewModel) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Settings ⚙️", color = TextDark, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = if(uiState.isDarkMode) Color(0xFF212121) else Color.White)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Dark Mode", color = if(uiState.isDarkMode) Color.White else TextDark, fontWeight = FontWeight.Bold)
                        Switch(
                            checked = uiState.isDarkMode,
                            onCheckedChange = { viewModel.setDarkMode(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = PinkPrimary)
                        )
                    }

                    Spacer(Modifier.height(16.dp))
                    HorizontalDivider(color = PinkLight)
                    Spacer(Modifier.height(16.dp))

                    Text("Sort Order", color = PinkPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = uiState.sortNewestFirst, onClick = { viewModel.setSortOrder(true) })
                        Text("Newest First", color = if(uiState.isDarkMode) Color.White else TextDark)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = !uiState.sortNewestFirst, onClick = { viewModel.setSortOrder(false) })
                        Text("Oldest First", color = if(uiState.isDarkMode) Color.White else TextDark)
                    }
                }
            }
        }

        item {
            Text("Device Info ✨", color = PinkPrimary, fontWeight = FontWeight.Bold)
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = if(uiState.isDarkMode) Color(0xFF212121) else PinkPrimary.copy(alpha = 0.05f))
            ) {
                Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    InfoRow("Model", uiState.deviceModel, uiState.isDarkMode)
                    InfoRow("Manufacturer", uiState.deviceManufacturer, uiState.isDarkMode)
                    InfoRow("OS Version", uiState.osVersion, uiState.isDarkMode)
                    HorizontalDivider(color = PinkLight)
                    InfoRow("Battery", "${uiState.batteryLevel}% ${if(uiState.isCharging) "(Charging)" else ""}", uiState.isDarkMode)
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String, isDarkMode: Boolean) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = if(isDarkMode) Color.LightGray else GrayText)
        Text(value, color = if(isDarkMode) Color.White else TextDark, fontWeight = FontWeight.Bold)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen(uiState: PocketWiseUiState, viewModel: PocketWiseViewModel, onBack: () -> Unit) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    showTimePicker = true
                }) { Text("Next") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val date = "05 Mei 2026" // Simplified
                    val time = "${timePickerState.hour}:${timePickerState.minute}"
                    viewModel.onReminderChanged("$date, $time")
                    showTimePicker = false
                }) { Text("OK") }
            },
            text = { TimePicker(state = timePickerState) }
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = PinkPrimary) }
                Text("Edit Pink Note 🌸", color = PinkPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }

        item {
            CustomTextField("Judul", uiState.noteTitleInput, viewModel::onTitleChanged)
        }

        item {
            CustomTextField("Deskripsi", uiState.noteDescriptionInput, viewModel::onDescriptionChanged)
        }

        item {
            Column {
                Text("Reminder", color = GrayText, fontSize = 12.sp, modifier = Modifier.padding(start = 12.dp, bottom = 4.dp))
                OutlinedTextField(
                    value = uiState.noteReminderInput,
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true },
                    enabled = false,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledContainerColor = Color.White,
                        disabledBorderColor = Color.LightGray,
                        disabledTextColor = TextDark
                    )
                )
            }
        }

        item {
            CustomTextField("Isi Catatan", uiState.noteContentInput, viewModel::onContentChanged, minLines = 5)
        }

        item {
            Button(
                onClick = { viewModel.translateAI() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (uiState.isAiLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                else Text("Translate with AI 🪄")
            }
        }

        if (uiState.translatedText.isNotBlank()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("AI Translation (English):", color = PinkPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(uiState.translatedText, color = TextDark)
                    }
                }
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray)
                ) {
                    Text("Batal", color = PinkPrimary)
                }
                Button(
                    onClick = {
                        val currentTitle = uiState.noteTitleInput
                        println("--- TOMBOL_SIMPAN_KLIK: Judul='$currentTitle' ---")
                        viewModel.saveNote()
                        println("--- NAVIGASI_BALIK_KE_HOME ---")
                        onBack()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Update", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun CustomTextField(label: String, value: String, onValueChange: (String) -> Unit, enabled: Boolean = true, minLines: Int = 1) {
    Column {
        Text(label, color = GrayText, fontSize = 12.sp, modifier = Modifier.padding(start = 12.dp, bottom = 4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            minLines = minLines,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = PinkPrimary,
                disabledBorderColor = Color.LightGray,
                disabledTextColor = TextDark
            )
        )
    }
}
