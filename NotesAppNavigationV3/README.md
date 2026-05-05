# TUGAS PRAKTIKUM MINGGU 8 - Pink Notes App Upgrade 🌸✨

Aplikasi ini telah di-upgrade dengan tema estetik Pink, fitur platform, dan implementasi **Dependency Injection** menggunakan **Koin**.

---

## 🎨 New Aesthetic UI (Pink Theme)
Aplikasi kini memiliki tampilan khusus "Pink Theme" yang estetik:
- **Floating Bottom Bar**: Navigasi melayang dengan sudut melengkung.
- **Custom Pink Palette**: Perpaduan Soft Pink, Hot Pink, dan Cream.
- **Rounded Aesthetic**: Semua kartu dan input memiliki desain *rounded* yang modern.

---

## 🏗️ Architecture Diagram

Project ini mengadopsi struktur modular dengan pemisahan tanggung jawab yang jelas:

```mermaid
graph TD
    subgraph "UI Layer (Compose - Pink Theme)"
        MainScreen --> NoteListScreen
        MainScreen --> SettingsScreen
        NoteListScreen --> NetworkIndicatorUI
        SettingsScreen --> DeviceInfoUI
    end

    subgraph "Dependency Injection (Koin)"
        AppModule[AppModule.kt]
    end

    subgraph "Logic & Data Layer"
        NoteViewModel --> NoteRepository
        NoteRepository --> SQLDelight[(Database)]
        NoteRepository --> DataStore[(Preferences)]
    end

    subgraph "Platform Features (Expect/Actual Abstraction)"
        DeviceInfo[DeviceInfo Interface]
        NetworkMonitor[NetworkMonitor Interface]
        BatteryInfo[BatteryInfo - Bonus]
        
        AndroidImpl[Android Implementation]
    end

    %% DI Flow
    AppModule -.-> |Inject| NoteViewModel
    AppModule -.-> |Inject| DeviceInfo
    AppModule -.-> |Inject| NetworkMonitor
    
    %% Flow
    NoteViewModel --> NetworkMonitor
    SettingsScreen --> DeviceInfo
    SettingsScreen --> BatteryInfo
```

---

## 📝 Fitur Utama & Deskripsi Tugas

1.  **Koin DI Setup**: Implementasi penuh Koin untuk menyuntikkan (inject) Repository, ViewModel, dan Platform Services secara otomatis.
2.  **DeviceInfo (expect/actual pattern)**: Abstraksi untuk mengambil informasi hardware (Model, Manufacturer, OS).
3.  **NetworkMonitor (expect/actual pattern)**: Memantau konektivitas secara reaktif menggunakan `callbackFlow` dan ditampilkan di UI secara real-time.
4.  **Aesthetic Settings Screen**: Menampilkan informasi sistem dan pengaturan aplikasi dalam desain kartu estetik.
5.  **Reactive Network UI**: Bar indikator "Connected to Pink Network" yang berubah warna sesuai status internet.
6.  **Full Injection**: Seluruh komponen dikelola dalam `appModule`.

---

## ✅ Rubrik Penilaian

| Komponen | Bobot | Status |
| :--- | :---: | :--- |
| **Koin DI Setup** | 25% | Terimplementasi (AppModule & NotesApplication) |
| **expect/actual Pattern** | 25% | Terimplementasi (Pola Interface & Implementasi Platform) |
| **Aesthetic UI Integration** | 20% | Terimplementasi (Home, Settings, Profile with Pink Theme) |
| **Architecture** | 20% | Clean separation & modular package structure |
| **Code Quality** | 10% | Clean code, Comments, & Documentation |
| **Bonus ⭐** | +10% | **BatteryInfo implementation (Level & Status)** |

---

## 📱 Screenshots

| Home | Profile | Settings |
| :---: | :---: | :---: |
| <img src="screenshot/Home.jpg" width="250"> | <img src="screenshot/Prodile.jpg" width="250"> | <img src="screenshot/Settings.jpg" width="250"> |

| Dark Mode | Detail Note |
| :---: | :---: |
| <img src="screenshot/Dark%20Mode.jpg" width="250"> | <img src="screenshot/Detail%20Note.jpg" width="250"> |

| Edit Note | Favorites |
| :---: | :---: |
| <img src="screenshot/Edit%20Note.jpg" width="250"> | <img src="screenshot/Favorites.jpg" width="250"> |

---

## 📸 Identitas Mahasiswa
- **Nama**: Mulya Delani
- **NIM**: 123140019

---
*Pengembangan Aplikasi Mobile*
