package com.example.spotifyplayer

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.coreandroid.util.ext.notificationManager

class SpotifyPlayerCancelNotificationService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        notificationManager.cancelAll()
    }

    override fun onDestroy() {
        super.onDestroy()
        notificationManager.cancelAll()
    }
}