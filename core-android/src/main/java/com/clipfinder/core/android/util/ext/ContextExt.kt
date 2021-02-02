package com.clipfinder.core.android.util.ext

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.DisplayMetrics
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.pwittchen.reactivenetwork.library.rx2.ConnectivityPredicate
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.spotify.sdk.android.player.Connectivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

fun Context.dpToPx(
    dp: Float
): Float = dp * (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)

fun Context.pxToDp(
    px: Float
): Float = px / (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)

val Context.screenOrientation: Int
    get() = resources.configuration.orientation

val Context.screenHeight: Int
    get() = resources.configuration.screenHeightDp

val Context.notificationManager: NotificationManagerCompat
    get() = NotificationManagerCompat.from(this)

inline fun Context.createAndRegisterReceiverFor(
    filter: IntentFilter,
    crossinline onReceive: (Context?, Intent?) -> Unit
): BroadcastReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) = onReceive(context, intent)
}.also {
    registerReceiver(it, filter)
}

@get:SuppressLint("MissingPermission")
val Context.networkConnectivity: Connectivity
    get() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return if (activeNetwork != null && activeNetwork.isConnected) {
            Connectivity.fromNetworkType(activeNetwork.type)
        } else {
            Connectivity.OFFLINE
        }
    }

val Context.isConnected: Boolean
    get() = networkConnectivity != Connectivity.OFFLINE

fun Context.isGranted(permission: String) = ContextCompat
    .checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

private typealias RxConnectivity = com.github.pwittchen.reactivenetwork.library.rx2.Connectivity

@SuppressLint("MissingPermission")
fun Context.observeNetworkConnectivity(
    connectedOnly: Boolean = true,
    onNext: (RxConnectivity) -> Unit
): Disposable = ReactiveNetwork.observeNetworkConnectivity(this)
    .onErrorReturn { RxConnectivity.state(NetworkInfo.State.DISCONNECTED).build() }
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
    .run {
        if (connectedOnly) filter(ConnectivityPredicate.hasState(NetworkInfo.State.CONNECTED))
        else this
    }
    .subscribe(onNext)

inline fun <reified T : RoomDatabase> Context.buildRoom(
    inMemory: Boolean = false,
    name: String = T::class.java.simpleName,
    noinline configure: (RoomDatabase.Builder<T>.() -> RoomDatabase.Builder<T>)? = null
): T {
    val builder = if (inMemory) {
        Room.inMemoryDatabaseBuilder(this, T::class.java)
    } else {
        Room.databaseBuilder(this, T::class.java, name)
    }
    if (configure != null) builder.configure()
    return builder.build()
}

fun Context.getBroadcastPendingIntent(
    intent: Intent,
    requestCode: Int = 0,
    flags: Int = 0
): PendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, flags)

fun Context.getActivityPendingIntent(
    intent: Intent,
    requestCode: Int = 0,
    flags: Int = 0
): PendingIntent = PendingIntent.getActivity(this, requestCode, intent, flags)
