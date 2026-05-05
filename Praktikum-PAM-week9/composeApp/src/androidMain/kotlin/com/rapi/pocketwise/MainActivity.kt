package com.rapi.pocketwise

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var batteryLevel by remember { mutableStateOf(0) }
            var isCharging by remember { mutableStateOf(false) }
            var isOnline by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                // Initial checks
                batteryLevel = getBatteryLevel()
                isCharging = getIsCharging()
                isOnline = checkInternetConnection()

                // Register receivers
                val batteryFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
                val batteryReceiver = object : BroadcastReceiver() {
                    override fun onReceive(context: Context?, intent: Intent?) {
                        batteryLevel = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
                        val status = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
                        isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                                    status == BatteryManager.BATTERY_STATUS_FULL
                    }
                }
                registerReceiver(batteryReceiver, batteryFilter)
                
                // Network check loop (simplified)
                while(true) {
                    isOnline = checkInternetConnection()
                    kotlinx.coroutines.delay(5000)
                }
            }

            App(
                geminiApiKey = BuildConfig.GEMINI_API_KEY,
                batteryLevel = batteryLevel,
                isCharging = isCharging,
                isOnline = isOnline,
                model = Build.MODEL,
                manufacturer = Build.MANUFACTURER,
                osVersion = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
            )
        }
    }

    private fun getBatteryLevel(): Int {
        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            applicationContext.registerReceiver(null, ifilter)
        }
        return batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
    }

    private fun getIsCharging(): Boolean {
        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            applicationContext.registerReceiver(null, ifilter)
        }
        val status = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        return status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
    }

    private fun checkInternetConnection(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
}
