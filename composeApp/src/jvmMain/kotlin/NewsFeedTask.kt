import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

// Model data untuk berita
data class News(val title: String, val category: String)

class NewsFeedSimulator {
    // 1. Implementasi Flow: Simulasi data berita tiap 2 detik [cite: 586]
    fun getNewsFlow(): Flow<News> = flow {
        val newsList = listOf(
            News("Update Kotlin 2.1", "Tech"),
            News("Info Beasiswa ITERA", "Campus"),
            News("Tips Coroutines & Flow", "Tech"),
            News("Workshop Mobile Dev", "Tech"),
            News("Lomba Coding Nasional", "Competition")
        )

        while (true) {
            emit(newsList.random()) // Memancarkan berita secara acak
            delay(2000) // Delay 2 detik [cite: 586]
        }
    }

    // 5. Coroutines: Ambil detail berita secara async [cite: 590]
    suspend fun fetchNewsDetail(title: String): String {
        delay(500) // Simulasi pengambilan data
        return "Detail: Informasi lengkap mengenai $title"
    }
}

// 4. StateFlow: Menyimpan jumlah berita yang sudah dibaca [cite: 589]
private val _readCount = MutableStateFlow(0)
val readCount: StateFlow<Int> = _readCount.asStateFlow()

fun main() = runBlocking {
    val simulator = NewsFeedSimulator()

    println("=== News Feed Simulator Dimulai ===\n")

    // Monitor StateFlow di coroutine terpisah
    launch {
        readCount.collect { count ->
            println("[STATE] Berita yang sudah dibaca: $count")
        }
    }

    // 2 & 3. Penggunaan Operators: Filter & Transform [cite: 587, 588]
    simulator.getNewsFlow()
        .filter { it.category == "Tech" } // Filter kategori 'Tech' saja [cite: 587]
        .map { "HEADLINE: ${it.title}" }  // Transform format teks [cite: 588]
        .take(5)                          // Batasi 5 berita untuk demo
        .collect { headline ->            // Terminal operator untuk memulai flow
            println("\n>>> $headline")

            // PERBAIKAN DI SINI: async butuh coroutineScope biar nggak error
            coroutineScope {
                val detail = async { simulator.fetchNewsDetail(headline) }
                println(detail.await())
            }

            _readCount.value++ // Update jumlah baca di StateFlow
        }

    println("\n=== Simulator Selesai ===")
}