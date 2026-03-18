package com.example.myprofileapp.data

data class ProfileUiState(
    // Data Profile Asli
    val name: String = "Mulya Delani",
    val bio: String = "Hidup Cuma Sekali, Pastikan Kamu Sudah Maksimalkan Hari Buat Cobain Semua Hal Yang Ada di Dunia",
    val email: String = "mulya.123140019@student.itera.ac.id",
    val phone: String = "085609156476",
    val location: String = "Lampung",

    // Data Form Edit Sementara
    val editName: String = "",
    val editBio: String = "",
    val editEmail: String = "",
    val editPhone: String = "",
    val editLocation: String = "",

    // Status UI
    val isDarkMode: Boolean = false,
    val isEditMode: Boolean = false
) {
    // Tombol save cuma nyala kalau field WAJIB (Nama & Bio) gak kosong dan ada yang berubah
    val isSaveEnabled: Boolean
        get() = editName.isNotBlank() && editBio.isNotBlank() &&
                (editName != name || editBio != bio || editEmail != email || editPhone != phone || editLocation != location)
}