package com.example.there.findclips

import android.app.Activity
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import com.example.coreandroid.util.Constants
import com.example.spotifyplayer.SpotifyPlayerCancelNotificationService
import com.example.there.findclips.di.AppInjector
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject


class FindClipsApp : Application(), HasActivityInjector {

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> = activityDispatchingAndroidInjector

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