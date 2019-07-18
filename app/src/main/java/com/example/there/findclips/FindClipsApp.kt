package com.example.there.findclips

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import com.example.coreandroid.util.Constants
import com.example.spotifyplayer.SpotifyPlayerCancelNotificationService
import com.example.there.findclips.module.*
import com.squareup.leakcanary.LeakCanary
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class FindClipsApp : Application() {

    override fun onCreate() {
        super.onCreate()

//        initLeakCanary()

        startService(Intent(this, SpotifyPlayerCancelNotificationService::class.java))
        createNotificationChannel()

        //TODO: maybe think about moving koin modules into their respective modules :D
        startKoin {
            androidContext(this@FindClipsApp)
            modules(listOf(
                    appModule, databaseModule, apiModule, repoModule,
                    soundCloudModule, spotifyModule, videosModule,
                    viewModelsModule
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