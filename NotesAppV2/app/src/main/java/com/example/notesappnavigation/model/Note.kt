package com.example.notesappnavigation.model

data class Note(
    val id: Long = 0,
    val title: String,
    val description: String,
    val content: String,
    val reminder: String,
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
