package com.example.coreandroid.util

import android.app.PendingIntent
import android.content.Context
import android.content.Intent

object PendingIntents {
    fun getActivity(
        context: Context?,
        intent: Intent?,
        requestCode: Int = 0,
        flags: Int = 0
    ): PendingIntent = PendingIntent.getActivity(context, requestCode, intent, flags)

    fun getBroadcast(
        context: Context?,
        intent: Intent?,
        requestCode: Int = 0,
        flags: Int = 0
    ): PendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, flags)
}
