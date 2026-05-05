package com.rapi.pocketwise.data.repository

import com.rapi.pocketwise.data.model.Note

interface NoteRepository {
    suspend fun translateText(text: String, targetLanguage: String = "English"): Result<String>
}
