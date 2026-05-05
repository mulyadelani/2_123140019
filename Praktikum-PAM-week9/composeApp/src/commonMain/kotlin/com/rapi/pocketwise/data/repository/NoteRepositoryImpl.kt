package com.rapi.pocketwise.data.repository

import com.rapi.pocketwise.data.remote.GeminiService

class NoteRepositoryImpl(
    private val geminiService: GeminiService
) : NoteRepository {

    override suspend fun translateText(text: String, targetLanguage: String): Result<String> {
        if (text.isBlank()) {
            return Result.failure(Exception("Teks tidak boleh kosong."))
        }

        val prompt = """
            Kamu adalah asisten penerjemah profesional.
            Tugasmu adalah menerjemahkan teks berikut ke dalam Bahasa $targetLanguage.
            
            Teks:
            $text
            
            Aturan:
            - Berikan hasil terjemahan saja, tanpa penjelasan tambahan.
            - Pertahankan gaya bahasa dan nada dari teks asli.
            - Jika teks sudah dalam Bahasa $targetLanguage, kembalikan teks yang sama.
        """.trimIndent()

        return geminiService.generateContent(prompt)
    }
}
