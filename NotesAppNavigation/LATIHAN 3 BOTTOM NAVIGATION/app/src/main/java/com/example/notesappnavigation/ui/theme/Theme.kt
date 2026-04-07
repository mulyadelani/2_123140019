package com.example.notesappnavigation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DesertOasisColorScheme = lightColorScheme(
    primary = TerracottaPrimary,
    secondary = SageSecondary,
    tertiary = CoffeeText,
    background = SandBackground,
    surface = SandSurface,
    onPrimary = SandBackground,
    onSecondary = SandBackground,
    onTertiary = SandBackground,
    onBackground = CoffeeText,
    onSurface = CoffeeText,
    outline = BorderColor
)

@Composable
fun NotesAppNavigationTheme(
    darkTheme: Boolean = false, // Gunakan tema terang untuk kesan Desert Oasis
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DesertOasisColorScheme,
        typography = Typography,
        content = content
    )
}