package com.example.there.findclips

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import com.bumptech.glide.request.target.ViewTarget
import com.example.core.android.spotify.di.spotifyCoreAndroidModule
import com.example.core.android.spotify.notification.PlaybackNotification
import com.example.coreandroid.di.epoxyModule
import com.example.spotify.album.di.spotifyAlbumModule
import com.example.spotify.category.di.spotifyCategoryModule
import com.example.spotify.dashboard.di.spotifyDashboardModule
import com.example.spotify.playlist.di.spotifyPlaylistModule
import com.example.spotifyapi.di.spotifyApiModule
import com.example.spotifyartist.di.spotifyArtistModule
import com.example.spotifyplayer.SpotifyPlayerCancelNotificationService
import com.example.there.findclips.module.*
import com.squareup.leakcanary.LeakCanary
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import timber.log.Timber.DebugTree

class ClipFinderApp : Application() {

    override fun onCreate() {
        super.onCreate()

//        initLeakCanary()
//        initNotifications()
        initKoin()

        ViewTarget.setTagId(R.id.glide_tag) //TODO: workaround for crashes caused by Glide - maybe try to remove this later

        if (BuildConfig.DEBUG) Timber.plant(DebugTree())
    }

    override fun onTerminate() {
        stopService(Intent(this, SpotifyPlayerCancelNotificationService::class.java))
        super.onTerminate()
    }

    private fun initLeakCanary() {
        if (!LeakCanary.isInAnalyzerProcess(this)) LeakCanary.install(this)
    }

    private fun initNotifications() {
        startService(Intent(this, SpotifyPlayerCancelNotificationService::class.java))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(
                    NotificationChannel(
                        PlaybackNotification.CHANNEL_ID,
                        getString(R.string.channel_name),
                        NotificationManager.IMPORTANCE_DEFAULT
                    ).apply {
                        description = getString(R.string.channel_description)
                    }
                )
        }
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@ClipFinderApp)
            modules(listOf(
                appModule, epoxyModule, databaseModule, apiModule, repoModule,
                soundCloudModule, spotifyModule, spotifyCoreAndroidModule, videosModule,
                viewModelsModule,
                spotifyApiModule,
                spotifyDashboardModule, spotifyAlbumModule, spotifyCategoryModule, spotifyPlaylistModule, spotifyArtistModule
            ))
        }
    }
}
