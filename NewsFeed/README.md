# Simulasi News Feed - Proyek KMP

**Penulis:** Mulya Delani (123140019)
**Mata Kuliah:** Mobile Application Development (Pertemuan 2)

## Penjelasan Proyek
Program ini adalah simulator "News Feed" berbasis konsol yang memanfaatkan **Kotlin Coroutines** dan **Kotlin Flow**. Tujuannya adalah untuk mendemonstrasikan pengolahan data secara asinkron dan reaktif dalam lingkungan Kotlin.

## Daftar Fitur & Teknik (Sesuai Kriteria)
1. **Penerapan Flow Builder (`flow { ... }`)**: Menghasilkan *stream* berita secara otomatis tiap 2 detik tanpa menghentikan proses lainnya (*non-blocking*).
2. **Manipulasi Data dengan Flow Operators**:
    - `.filter { }`: Hanya meloloskan berita yang termasuk dalam kategori "Tech".
    - `.map { }`: Mengubah format tampilan berita menjadi `HEADLINE: [Judul Berita]`.
3. **Manajemen State dengan StateFlow (`MutableStateFlow`)**: Mengelola data jumlah berita yang telah dibaca secara reaktif dan diperbarui secara langsung.
4. **Eksekusi Paralel dengan Coroutines (`async/await`)**: Menjalankan simulasi pengambilan detail berita dari jaringan secara bersamaan agar aliran utama tetap lancar.
5. **Keamanan Konkurensi dengan CoroutineScope (`coroutineScope { }`)**: Menerapkan *structured concurrency* untuk menjaga keamanan eksekusi asinkron pada bagian terminal operator.

## Panduan Pengoperasian
Aplikasi ini dikembangkan dengan struktur Kotlin Multiplatform (KMP). Berikut langkah untuk menjalankannya sebagai aplikasi konsol JVM:
1. Buka proyek menggunakan IDE (Android Studio atau IntelliJ IDEA).
2. Cari file `src/jvmMain/kotlin/NewsFeedTask.kt`.
3. Tekan tombol *Run* (ikon segitiga hijau) yang terletak di sisi fungsi `main()`.
4. Pilih opsi **Run 'NewsFeedTaskKt'**.
5. Hasil simulasi bisa dipantau langsung melalui jendela *Run/Terminal* di IDE Anda.
