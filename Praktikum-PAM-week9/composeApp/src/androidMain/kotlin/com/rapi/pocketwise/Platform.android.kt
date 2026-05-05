package com.rapi.pocketwise

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val model: String = Build.MODEL
    override val manufacturer: String = Build.MANUFACTURER
    override val osVersion: String = Build.VERSION.RELEASE
    override val batteryLevel: Int = 0
    override val isCharging: Boolean = false
    override val isOnline: Boolean = false
}

actual fun getPlatform(): Platform = AndroidPlatform()