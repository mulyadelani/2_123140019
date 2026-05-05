package com.rapi.pocketwise.data.model

data class Note(
    val id: String,
    val title: String,
    val description: String,
    val reminder: String,
    val content: String,
    val isFavorite: Boolean = false,
    val date: String
)
