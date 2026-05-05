# Tugas Praktikum 9

#### Nama : Mulya Delani
#### NIM  : 123140019

## Deskripsi
Pengembangan aplikasi **Pink Notes**, yaitu aplikasi pencatat catatan pribadi berbasis **Kotlin Multiplatform** yang terintegrasi dengan **Gemini API** untuk fitur penerjemahan otomatis.

Aplikasi ini memungkinkan pengguna untuk membuat, mengedit, dan mengelola catatan dengan antarmuka yang ramah pengguna. Fitur utama yang membedakan adalah integrasi AI yang dapat menerjemahkan isi catatan secara langsung ke dalam Bahasa Inggris.

## Fitur Utama
- **Manajemen Catatan**: Membuat, memperbarui, dan menghapus catatan (CRUD).
- **Favorite**: Menandai catatan tertentu sebagai favorit untuk akses cepat.
- **AI Translation**: Menerjemahkan isi catatan ke Bahasa Inggris menggunakan **Gemini API**.
- **Device Info**: Menampilkan informasi teknis perangkat seperti model, manufaktur, versi OS, dan level baterai.
- **Connectivity Monitoring**: Mendeteksi dan menampilkan status koneksi internet (Online/Offline).
- **Mode Gelap (Dark Mode)**: Dukungan tema gelap untuk kenyamanan mata.
- **Reminder**: Menambahkan pengingat waktu pada catatan menggunakan Date & Time Picker.
- **Pencarian**: Mencari catatan berdasarkan judul.

## AI Integration
Aplikasi ini memanfaatkan **Gemini API** untuk memberikan kemampuan bahasa pada catatan pengguna.

Alur penggunaan AI:
1. Pengguna masuk ke mode edit catatan.
2. Pengguna memasukkan teks catatan dalam Bahasa Indonesia (atau bahasa lain).
3. Pengguna menekan tombol **"Translate with AI"**.
4. Aplikasi mengirimkan teks tersebut ke model Gemini dengan instruksi penerjemahan khusus.
5. Hasil terjemahan diterima dan ditampilkan di bawah teks asli sebagai referensi.

## Prompt Engineering
Prompt yang dikirim ke Gemini dirancang secara spesifik:
- **Role**: Bertindak sebagai asisten penerjemah profesional.
- **Task**: Menerjemahkan teks input ke dalam Bahasa Inggris.
- **Instruksi Khusus**:
  - Hanya memberikan hasil terjemahan tanpa teks tambahan/penjelasan.
  - Mempertahankan nada dan gaya bahasa asli.
  - Menangani kondisi jika teks sudah dalam bahasa target.

## Error Handling
Aplikasi telah dilengkapi dengan penanganan kesalahan untuk memastikan pengalaman pengguna yang baik:
- Validasi input (teks tidak boleh kosong).
- Penanganan API Key yang tidak valid atau belum dikonfigurasi.
- Deteksi masalah jaringan atau timeout saat memanggil API.
- Penanganan batas penggunaan (Rate Limit/Quota) pada API.

## Cara Menjalankan (Android Studio)
1. Buka folder project menggunakan Android Studio.
2. Tunggu proses **Gradle Sync** sampai selesai.
3. Pastikan API Key Gemini sudah terkonfigurasi (biasanya di `local.properties`).
4. Jalankan aplikasi dengan menekan tombol **Run**.
5. Pilih emulator atau perangkat Android yang terhubung.

## Screenshot Aplikasi

| Before Translate | After Translate |
| :---: | :---: |
| <img src="./Screenshot/Before%20Translate.jpg" width="300" /> | <img src="./Screenshot/After%20Translate.jpg" width="300" /> |
