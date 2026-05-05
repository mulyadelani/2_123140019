package com.rapi.pocketwise

interface Platform {
    val name: String
    val model: String
    val manufacturer: String
    val osVersion: String
    val batteryLevel: Int
    val isCharging: Boolean
    val isOnline: Boolean
}

expect fun getPlatform(): Platform
