package com.clipfinder.core.android.spotify.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.clipfinder.core.android.spotify.preferences.SpotifyPreferences
import org.koin.core.KoinComponent
import org.koin.core.inject

class PublicAccessTokenExpiredBroadcastReceiver : BroadcastReceiver(), KoinComponent {
    private val preferences: SpotifyPreferences by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        preferences.publicAccessToken = null
    }
}
