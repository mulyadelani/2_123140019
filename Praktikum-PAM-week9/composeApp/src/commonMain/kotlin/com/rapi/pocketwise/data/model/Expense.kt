package com.rapi.pocketwise.data.model

data class Expense(
    val id: String,
    val amount: Int,
    val category: ExpenseCategory,
    val note: String,
    val date: String
)

enum class ExpenseCategory(val label: String) {
    MAKANAN("Makanan"),
    KOPI_JAJAN("Kopi/Jajan"),
    TRANSPORTASI("Transportasi"),
    BELANJA("Belanja"),
    PENDIDIKAN("Pendidikan"),
    LAINNYA("Lainnya")
}
