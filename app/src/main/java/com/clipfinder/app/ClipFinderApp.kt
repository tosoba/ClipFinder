package com.clipfinder.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.bumptech.glide.request.target.ViewTarget
import com.clipfinder.app.module.appModule
import com.clipfinder.core.android.di.coreAndroidNetworkingModule
import com.clipfinder.core.android.di.epoxyModule
import com.clipfinder.core.android.soundcloud.di.soundCloudCoreAndroidModule
import com.clipfinder.core.android.spotify.di.spotifyCoreAndroidModule
import com.clipfinder.core.android.util.ext.notificationManager
import com.clipfinder.core.android.youtube.di.youtubeCoreAndroidModule
import com.clipfinder.core.notification.PlaybackNotification
import com.clipfinder.core.soundcloud.di.soundCloudCoreModule
import com.clipfinder.core.spotify.di.spotifyCoreModule
import com.clipfinder.core.youtube.di.youtubeCoreModule
import com.clipfinder.soundcloud.api.di.soundCloudApiModule
import com.clipfinder.spotify.api.di.spotifyApiModule
import com.github.mikephil.charting.utils.Utils
import io.reactivex.plugins.RxJavaPlugins
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import timber.log.Timber.DebugTree

class ClipFinderApp : Application() {
    override fun onCreate() {
        super.onCreate()

        initTimber()
        initNotifications()
        initKoin()

        RxJavaPlugins.setErrorHandler { Timber.e(it, "RX") }
        Utils.init(this)

        // TODO: workaround for crashes caused by Glide - maybe try to remove this later
        ViewTarget.setTagId(R.id.glide_tag)
    }

    override fun onTerminate() {
        notificationManager.cancelAll()
        super.onTerminate()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) Timber.plant(DebugTree())
    }

    private fun initNotifications() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        getSystemService(NotificationManager::class.java)
            .createNotificationChannel(
                NotificationChannel(
                        PlaybackNotification.CHANNEL_ID,
                        getString(R.string.channel_name),
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    .apply {
                        description = getString(R.string.channel_description)
                        setSound(null, null)
                    }
            )
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@ClipFinderApp)
            modules(
                listOf(
                    appModule,
                    epoxyModule,
                    coreAndroidNetworkingModule,
                    spotifyApiModule,
                    spotifyCoreAndroidModule,
                    spotifyCoreModule,
                    soundCloudApiModule,
                    soundCloudCoreAndroidModule,
                    soundCloudCoreModule,
                    youtubeCoreModule,
                    youtubeCoreAndroidModule
                )
            )
        }
    }
}
