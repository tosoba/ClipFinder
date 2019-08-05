package com.example.there.findclips

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import com.bumptech.glide.request.target.ViewTarget
import com.example.coreandroid.util.Constants
import com.example.spotifyapi.spotifyApiModule
import com.example.spotifydashboard.di.spotifyDashboardModule
import com.example.spotifyplayer.SpotifyPlayerCancelNotificationService
import com.example.there.findclips.module.*
import com.squareup.leakcanary.LeakCanary
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class ClipFinderApp : Application() {

    override fun onCreate() {
        super.onCreate()

//        initLeakCanary()

        startService(Intent(this, SpotifyPlayerCancelNotificationService::class.java))
        createNotificationChannel()

        ViewTarget.setTagId(R.id.glide_tag) //TODO: workaround for crashes caussed by Glide - maybe try to remove this later

        //TODO: maybe think about moving koin modules into their respective modules :D
        startKoin {
            androidContext(this@ClipFinderApp)
            modules(listOf(
                    appModule, databaseModule, apiModule, repoModule,
                    soundCloudModule, spotifyModule, videosModule,
                    viewModelsModule,
                    spotifyApiModule,
                    spotifyDashboardModule
            ))
        }
    }

    override fun onTerminate() {
        stopService(Intent(this, SpotifyPlayerCancelNotificationService::class.java))
        super.onTerminate()
    }

    private fun initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) return
        LeakCanary.install(this)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getSystemService(NotificationManager::class.java).run {
                createNotificationChannel(NotificationChannel(
                        Constants.NOTIFICATION_CHANNEL_ID,
                        getString(R.string.channel_name),
                        NotificationManager.IMPORTANCE_DEFAULT
                ).apply { description = getString(R.string.channel_description) })
            }
        }
    }
}