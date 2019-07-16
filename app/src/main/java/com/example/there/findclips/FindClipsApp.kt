package com.example.there.findclips

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import com.example.coreandroid.util.Constants
import com.example.spotifyplayer.SpotifyPlayerCancelNotificationService
import com.example.there.findclips.di.AppInjector
import com.example.there.findclips.di.DaggerAppComponent
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication


class FindClipsApp : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = DaggerAppComponent
            .builder()
            .application(this)
            .create(this)

    override fun onCreate() {
        super.onCreate()

//        initLeakCanary()

        startService(Intent(this, SpotifyPlayerCancelNotificationService::class.java))

        createNotificationChannel()

        AppInjector.init(this)
    }

    override fun onTerminate() {
        stopService(Intent(this, SpotifyPlayerCancelNotificationService::class.java))
        super.onTerminate()
    }

    private fun initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID, getString(R.string.channel_name), NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = getString(R.string.channel_description)
            }
            getSystemService(NotificationManager::class.java).run {
                createNotificationChannel(channel)
            }
        }
    }
}