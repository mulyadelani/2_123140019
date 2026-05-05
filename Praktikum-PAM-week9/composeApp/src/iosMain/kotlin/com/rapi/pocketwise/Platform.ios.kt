package com.rapi.pocketwise

import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val model: String = UIDevice.currentDevice.model
    override val manufacturer: String = "Apple"
    override val osVersion: String = UIDevice.currentDevice.systemVersion
    override val batteryLevel: Int = 0
    override val isCharging: Boolean = false
    override val isOnline: Boolean = false
}

actual fun getPlatform(): Platform = IOSPlatform()