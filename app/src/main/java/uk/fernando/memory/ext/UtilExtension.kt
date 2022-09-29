package uk.fernando.memory.ext

import android.content.Context
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.navigation.NavController

val Any.TAG: String
    get() {
        val tag = javaClass.simpleName
        return tag.take(23)
    }

fun NavController.safeNav(direction: String) {
    kotlin.runCatching {
        this.navigate(direction)
    }
}

fun MediaPlayer.playAudio(enableSound: Boolean = true) {
    if (!enableSound)
        return

    kotlin.runCatching {
        if (isPlaying) {
            stop()
            prepare()
        }
        start()
    }
}

fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            return true
        }
    }
    return false
}