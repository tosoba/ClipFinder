package com.clipfinder.core.android.spotify.ext

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.spotify.sdk.android.player.Connectivity

@get:SuppressLint("MissingPermission")
val Context.networkConnectivity: Connectivity
    get() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        @SuppressLint("MissingPermission")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return Connectivity.OFFLINE
            val capabilities =
                connectivityManager.getNetworkCapabilities(network) ?: return Connectivity.OFFLINE
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ->
                    Connectivity.WIRELESS
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->
                    Connectivity.MOBILE
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ->
                    Connectivity.WIRED
                else -> Connectivity.OFFLINE
            }
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return Connectivity.OFFLINE
            return if (networkInfo.isConnected) {
                Connectivity.fromNetworkType(networkInfo.type)
            } else {
                Connectivity.OFFLINE
            }
        }
    }
