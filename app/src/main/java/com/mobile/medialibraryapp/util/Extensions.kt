package com.mobile.medialibraryapp.util

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

import android.os.Parcelable
import android.provider.Settings
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Context.hasNetworkConnection(): Boolean {
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    var isConnected = false
    connectivityManager.allNetworks.forEach { network ->
        val networkCapability = connectivityManager.getNetworkCapabilities(network)
        networkCapability?.let {
            if (it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                isConnected = true
                return@forEach
            }
        }
    }
    return isConnected
}

fun Context.showNetworkSettings() {
    val chooserIntent = Intent.createChooser(
        getSettingsIntent(Settings.ACTION_DATA_ROAMING_SETTINGS),
        "Complete action using"
    )
    val networkIntents = ArrayList<Intent>()
    networkIntents.add(getSettingsIntent(Settings.ACTION_WIFI_SETTINGS))
    chooserIntent.putExtra(
        Intent.EXTRA_INITIAL_INTENTS,
        networkIntents.toTypedArray<Parcelable>()
    )
    chooserIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivityBySettings(this, chooserIntent)
}

private fun getSettingsIntent(settings: String): Intent {
    return Intent(settings)
}

private fun startActivityBySettings(context: Context, intent: Intent) {
    context.startActivity(intent)
}

fun String.isValidEmail(): Boolean {
    val emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    return this.matches(emailPattern.toRegex())
}

fun String.validatePassword(): String? {
    return when {
        this.length < 8 -> "Password must be at least 8 characters long"
        !this.any { it.isUpperCase() } -> "Password must contain at least one uppercase letter"
        !this.any { it.isLowerCase() } -> "Password must contain at least one lowercase letter"
        !this.any { it.isDigit() } -> "Password must contain at least one digit"
        !this.any { it in "@#\$%^&+=!" } -> "Password must contain at least one special character (@#\$%^&+=!)"
        else -> null
    }
}

fun String.parseDateToMillis(): Long {
    return try {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        formatter.parse(this)?.time ?: 0L
    } catch (_: Exception) {
        0L
    }
}

fun Long.toReadableSize(): String {
    val kb = 1024
    val mb = kb * 1024
    val gb = mb * 1024

    return when {
        this < kb -> "$this B"
        this < mb -> "%.2f KB".format(this.toDouble() / kb)
        this < gb -> "%.2f MB".format(this.toDouble() / mb)
        else -> "%.2f GB".format(this.toDouble() / gb)
    }
}

fun Long.toFormattedDate(): String {
    val date = Date(this * 1000)
    val dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
    return dateFormat.format(date)
}