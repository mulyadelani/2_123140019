package com.example.tugas2_123140019

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Tugas2_123140019",
    ) {
        App()
    }
}