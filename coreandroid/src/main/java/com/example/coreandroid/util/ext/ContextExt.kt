package com.example.coreandroid.util.ext

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.support.v4.app.NotificationManagerCompat
import android.util.DisplayMetrics
import com.spotify.sdk.android.player.Connectivity

fun Context.dpToPx(dp: Float): Float = dp * (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)

fun Context.pxToDp(px: Float): Float = px / (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)

val Context.screenOrientation: Int
    get() = resources.configuration.orientation

val Context.screenHeight: Int
    get() = resources.configuration.screenHeightDp

val Context.notificationManager: NotificationManagerCompat
    get() = NotificationManagerCompat.from(this)

inline fun Context.registerReceiverFor(
        filter: IntentFilter, crossinline onReceive: (Context?, Intent?) -> Unit
): BroadcastReceiver {
    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) = onReceive(context, intent)
    }
    registerReceiver(receiver, filter)
    return receiver
}

@get:SuppressLint("MissingPermission")
val Context.networkConnectivity: Connectivity
    get() {
        val connectivityManager: ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return if (activeNetwork != null && activeNetwork.isConnected) Connectivity.fromNetworkType(activeNetwork.type)
        else Connectivity.OFFLINE
    }