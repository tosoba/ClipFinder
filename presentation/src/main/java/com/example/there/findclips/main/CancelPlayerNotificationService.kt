package com.example.there.findclips.main

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.there.findclips.util.ext.notificationManager

class CancelPlayerNotificationService : Service() {

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