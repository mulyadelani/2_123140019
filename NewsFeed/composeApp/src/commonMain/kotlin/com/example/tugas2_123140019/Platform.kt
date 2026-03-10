package com.example.tugas2_123140019

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform