package com.example.coreandroid.util.ext

import android.content.Context
import android.support.v4.app.NotificationManagerCompat
import android.util.DisplayMetrics

fun Context.dpToPx(dp: Float): Float = dp * (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)

fun Context.pxToDp(px: Float): Float = px / (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)

val Context.screenOrientation: Int
    get() = resources.configuration.orientation

val Context.screenHeight: Int
    get() = resources.configuration.screenHeightDp

val Context.notificationManager: NotificationManagerCompat
    get() = NotificationManagerCompat.from(this)
