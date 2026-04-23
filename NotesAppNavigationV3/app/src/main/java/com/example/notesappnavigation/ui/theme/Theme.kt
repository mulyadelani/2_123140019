package com.example.notesappnavigation.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PinkPrimary,
    secondary = SoftPink,
    tertiary = DeepPink,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = Color.DarkGray,
    onSecondary = Color.DarkGray,
    onBackground = Color(0xFFFCE4EC),
    onSurface = Color(0xFFFCE4EC)
)

private val LightColorScheme = lightColorScheme(
    primary = DeepPink,
    secondary = PinkPrimary,
    tertiary = SoftPink,
    background = CreamBackground,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.DarkGray,
    onTertiary = Color.DarkGray,
    onBackground = Color(0xFF880E4F), // Muted dark pink for text
    onSurface = Color(0xFFAD1457)     // Deep pink for surface text
)

@Composable
fun NotesAppNavigationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
