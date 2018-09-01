package com.example.there.findclips.util.ext

import android.content.Context
import android.util.DisplayMetrics

fun Context.dpToPx(dp: Float): Float {
    val metrics = resources.displayMetrics
    return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

val Context.screenOrientation: Int
    get() = resources.configuration.orientation