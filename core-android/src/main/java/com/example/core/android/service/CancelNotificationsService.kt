package com.example.core.android.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.core.android.util.ext.notificationManager

class CancelNotificationsService : Service() {
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
