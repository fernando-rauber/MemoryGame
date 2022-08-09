package uk.fernando.memory.ext

import android.content.Context
import android.os.VibrationEffect
import android.os.VibratorManager
import android.text.format.DateUtils
import androidx.navigation.NavController

val Any.TAG: String
    get() {
        val tag = javaClass.simpleName
        return tag.take(23)
    }

fun NavController.safeNav(direction: String) {
    try {
        this.navigate(direction)
    } catch (e: Exception) {
    }
}

fun Context.vibrate() {
    kotlin.runCatching {
        (this.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager)
            .defaultVibrator.vibrate(VibrationEffect.createOneShot(2000, VibrationEffect.DEFAULT_AMPLITUDE))
    }
}