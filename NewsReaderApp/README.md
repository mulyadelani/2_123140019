# 🌸 InspirasiDaily - Jurnal Lifestyle & Tren

InspirasiDaily adalah aplikasi pembaca konten modern berbasis Android yang dirancang khusus untuk memberikan inspirasi harian mulai dari fashion, self-care, hingga tips keuangan. Dibangun menggunakan **Jetpack Compose**, aplikasi ini menawarkan antarmuka yang elegan dan lembut, sangat cocok untuk pengguna yang menyukai estetika minimalis.

## 🎀 Fitur Utama
- **Feminine UI & Soft Mode:** Tampilan cantik dengan palet warna Pink Pastel & Deep Purple yang memberikan kesan hangat dan nyaman.
- **Offline Reading (Room Database):** Tetap bisa membaca artikel inspiratif favoritmu meskipun sedang offline berkat fitur caching yang cerdas.
- **Smooth Shimmer Loading:** Transisi loading yang halus menggunakan efek *shimmer* untuk pengalaman pengguna yang lebih premium.
- **In-App Web View:** Membaca sumber artikel asli secara langsung tanpa perlu meninggalkan aplikasi menggunakan Chrome Custom Tabs.
- **Dynamic Content Refresh:** Dapatkan konten-konten inspiratif baru setiap kali kamu melakukan penyegaran (*refresh*) halaman.

## 🛠️ Tech Stack
- **Jetpack Compose (Material 3):** Fondasi UI deklaratif untuk tampilan yang modern dan responsif.
- **Ktor Client:** Menangani komunikasi data dengan performa yang efisien.
- **Room Persistence:** Penyimpanan lokal untuk memastikan konten selalu tersedia kapan saja.
- **Coil:** Pemuatan gambar yang cepat untuk menampilkan visual artikel yang menarik.
- **MVVM Architecture:** Struktur kode yang terorganisir untuk kemudahan pengembangan lebih lanjut.

## 📸 Tampilan Aplikasi
Nikmati setiap detail visual yang dirancang untuk kenyamanan mata:

| Beranda Inspirasi | Detail Artikel | Tampilan Web |
| :---: | :---: | :---: |
| ![Home](Screenshot/) | ![Detail](Screenshot/) | ![Browser](Screenshot/) |

## 🕯️ Manajemen Data
Aplikasi ini menerapkan **Repository Pattern** dengan pendekatan *Local-First*:
1. **Local Storage:** Memprioritaskan pemuatan data dari database lokal untuk kecepatan akses.
2. **Dynamic Mock Remote:** Mensimulasikan pengambilan konten tren terbaru melalui `NewsRepositoryImpl`.
3. **Smart Sync:** Sinkronisasi otomatis antara data terbaru dengan cache lokal untuk mendukung penggunaan luring.

---

## 👤 Identitas Pengembang
- **Nama:** Mulya Delani
- **NIM:** 123140019
