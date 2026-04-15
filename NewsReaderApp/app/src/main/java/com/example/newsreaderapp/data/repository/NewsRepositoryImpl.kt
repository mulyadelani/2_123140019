package com.example.newsreaderapp.data.repository

import com.example.newsreaderapp.data.local.ArticleDao
import com.example.newsreaderapp.data.local.ArticleEntity
import com.example.newsreaderapp.domain.Article
import com.example.newsreaderapp.domain.repository.NewsRepository
import com.example.newsreaderapp.domain.repository.Resource
import io.ktor.client.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NewsRepositoryImpl(
    private val client: HttpClient,
    private val dao: ArticleDao
) : NewsRepository {

    // Kumpulan Berita Lifestyle & Inspirasi (Mock Pool) untuk nuansa yang lebih soft
    private val beritaPool = listOf(
        ArticleEntity(1, "Tren Fashion Spring 2024: Sentuhan Pastel Kembali Hits!", "Warna-warna lembut seperti peach fuzz dan lavender diprediksi akan mendominasi panggung fashion tahun ini.", "https://images.unsplash.com/photo-1490481651871-ab68de25d43d?q=80&w=600&auto=format&fit=crop", "https://www.vogue.com"),
        ArticleEntity(2, "5 Tips Skincare Rutin untuk Wajah Glowing Alami", "Rahasia kulit sehat ternyata berawal dari hidrasi yang cukup dan pemilihan produk yang tepat sesuai jenis kulit.", "https://images.unsplash.com/photo-1556228720-195a672e8a03?q=80&w=600&auto=format&fit=crop", "https://www.femaledaily.com"),
        ArticleEntity(3, "Kisah Inspiratif: Founder Startup Lokal yang Sukses di Usia Muda", "Perjalanan jatuh bangun sosok perempuan hebat dalam membangun bisnis kreatif dari nol hingga kancah internasional.", "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?q=80&w=600&auto=format&fit=crop", "https://www.forbes.com"),
        ArticleEntity(4, "Resep Dessert Sehat: Strawberry Oat Smoothies", "Cara praktis membuat sarapan lezat sekaligus menyehatkan bagi kamu yang memiliki mobilitas tinggi.", "https://images.unsplash.com/photo-1511690656952-34342bb7c2f2?q=80&w=600&auto=format&fit=crop", "https://www.cookpad.com"),
        ArticleEntity(5, "Self-Care: Pentingnya Menjaga Kesehatan Mental di Era Digital", "Menyisihkan waktu untuk 'me time' bukan sekadar kemewahan, melainkan kebutuhan demi keseimbangan hidup.", "https://images.unsplash.com/photo-1506126613408-eca07ce68773?q=80&w=600&auto=format&fit=crop", "https://www.psychologytoday.com"),
        ArticleEntity(6, "Rekomendasi Cafe Aesthetic di Jakarta untuk Weekend List", "Tempat-tempat instagramable dengan suasana tenang, cocok untuk kumpul bersama sahabat.", "https://images.unsplash.com/photo-1554118811-1e0d58224f24?q=80&w=600&auto=format&fit=crop", "https://www.zomato.com"),
        ArticleEntity(7, "Eksplorasi Warna Interior Rumah: Soft Pink & Gold", "Paduan warna yang memberikan kesan mewah namun tetap hangat dan nyaman di dalam rumah.", "https://images.unsplash.com/photo-1513694203232-719a280e022f?q=80&w=600&auto=format&fit=crop", "https://www.architecturaldigest.com"),
        ArticleEntity(8, "Pentingnya Literasi Keuangan Bagi Perempuan Modern", "Mengelola finansial pribadi secara mandiri adalah langkah awal menuju kebebasan di masa depan.", "https://images.unsplash.com/photo-1579621970795-87facc2f976d?q=80&w=600&auto=format&fit=crop", "https://www.cnbc.com")
    )

    override fun getArticles(): Flow<Resource<List<Article>>> = flow {
        emit(Resource.Loading())

        val localArticles = dao.getAllArticles().map { it.toArticle() }
        emit(Resource.Loading(data = localArticles))

        try {
            delay(1000)
            
            val randomArticles = beritaPool.shuffled().take(5)

            dao.clearArticles()
            dao.insertArticles(randomArticles)

            val updatedArticles = dao.getAllArticles().map { it.toArticle() }
            emit(Resource.Success(updatedArticles))

        } catch (e: Exception) {
            emit(Resource.Error(
                message = "Gagal memuat inspirasi terbaru. Menampilkan konten tersimpan.",
                data = localArticles
            ))
        }
    }

    override suspend fun getArticleById(id: Int): Article? {
        return dao.getArticleById(id)?.toArticle()
    }

    private fun ArticleEntity.toArticle() = Article(
        id = id,
        title = title,
        description = content,
        imageUrl = imageUrl,
        url = url
    )
}
