package com.example.there.findclips

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import com.bumptech.glide.request.target.ViewTarget
import com.clipfinder.core.android.soundcloud.di.soundCloudCoreAndroidModule
import com.clipfinder.core.android.youtube.di.youtubeCoreAndroidModule
import com.clipfinder.core.spotify.di.spotifyCoreModule
import com.clipfinder.core.youtube.di.youtubeCoreModule
import com.example.core.android.di.coreAndroidNetworkingModule
import com.example.core.android.di.epoxyModule
import com.example.core.android.spotify.di.spotifyCoreAndroidModule
import com.example.core.android.spotify.notification.PlaybackNotification
import com.example.soundclouddashboard.di.soundCloudDashboardModule
import com.example.spotify.account.playlist.di.spotifyAccountPlaylistsModule
import com.example.spotify.account.saved.di.spotifyAccountSavedModule
import com.example.spotify.account.top.di.spotifyAccountTopModule
import com.example.spotify.album.di.spotifyAlbumModule
import com.example.spotify.artist.di.spotifyArtistModule
import com.example.spotify.category.di.spotifyCategoryModule
import com.example.spotify.dashboard.di.spotifyDashboardModule
import com.example.spotify.playlist.di.spotifyPlaylistModule
import com.example.spotify.search.di.spotifySearchModule
import com.example.spotifyapi.di.spotifyApiModule
import com.example.spotifyplayer.SpotifyPlayerCancelNotificationService
import com.example.there.findclips.module.*
import com.github.mikephil.charting.utils.Utils
import com.squareup.leakcanary.LeakCanary
import io.reactivex.plugins.RxJavaPlugins
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import timber.log.Timber.DebugTree

class ClipFinderApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initTimber()
//        initLeakCanary()
//        initNotifications()
        initKoin()

        RxJavaPlugins.setErrorHandler { Timber.e(it, "RX") }
        Utils.init(this)
        ViewTarget.setTagId(R.id.glide_tag) //TODO: workaround for crashes caused by Glide - maybe try to remove this later
    }

    override fun onTerminate() {
        stopService(Intent(this, SpotifyPlayerCancelNotificationService::class.java))
        super.onTerminate()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) Timber.plant(DebugTree())
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
                apiModule, repoModule,
                soundCloudModule, videosModule,
                viewModelsModule,

                appModule, epoxyModule, coreAndroidNetworkingModule,
                spotifyApiModule, com.clipfinder.spotify.api.di.spotifyApiModule,
                spotifyCoreAndroidModule, spotifyCoreModule,
                spotifyDashboardModule, spotifySearchModule,
                spotifyAccountTopModule, spotifyAccountPlaylistsModule, spotifyAccountSavedModule,
                spotifyAlbumModule, spotifyCategoryModule, spotifyPlaylistModule, spotifyArtistModule,

                soundCloudCoreAndroidModule,
                soundCloudDashboardModule,

                youtubeCoreModule, youtubeCoreAndroidModule
            ))
        }
    }
}
