package com.example.there.findclips

import android.app.Application
import com.example.there.findclips.di.AppComponent
import com.example.there.findclips.di.DaggerAppComponent
import com.example.there.findclips.di.dashboard.DashboardModule
import com.example.there.findclips.di.dashboard.DashboardSubComponent
import com.example.there.findclips.di.favourites.FavouritesModule
import com.example.there.findclips.di.favourites.FavouritesSubComponent
import com.example.there.findclips.di.modules.AppModule
import com.example.there.findclips.di.modules.DataModule
import com.example.there.findclips.di.modules.NetworkModule
import com.example.there.findclips.di.search.SearchModule
import com.example.there.findclips.di.search.SearchSubComponent
import com.example.there.findclips.di.videos.VideosModule
import com.example.there.findclips.di.videos.VideosSubComponent
import com.squareup.leakcanary.LeakCanary

class FindClipsApp : Application() {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        initLeakCanary()

        initAppComponent()
    }

    private fun initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)
    }

    private fun initAppComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(applicationContext))
                .dataModule(DataModule())
                .networkModule(NetworkModule(
                        spotifyApiBaseUrl = getString(R.string.spotify_base_url),
                        accessTokenBaseUrl = getString(R.string.access_token_base_url),
                        spotifyChartsBaseUrl = getString(R.string.spotify_charts_base_url),
                        youtubeBaseUrl = getString(R.string.youtube_base_url))
                ).build()
    }

    private var dashboardSubComponent: DashboardSubComponent? = null
    fun createDashboardComponent(): DashboardSubComponent {
        dashboardSubComponent = appComponent.plus(DashboardModule())
        return dashboardSubComponent!!
    }

    fun releaseDashboardComponent() {
        dashboardSubComponent = null
    }

    private var favouritesSubComponent: FavouritesSubComponent? = null
    fun createFavouritesComponent(): FavouritesSubComponent {
        favouritesSubComponent = appComponent.plus(FavouritesModule())
        return favouritesSubComponent!!
    }

    fun releaseFavouritesComponent() {
        favouritesSubComponent = null
    }

    private var searchSubComponent: SearchSubComponent? = null
    fun createSearchComponent(): SearchSubComponent {
        searchSubComponent = appComponent.plus(SearchModule())
        return searchSubComponent!!
    }

    fun releaseSearchComponent() {
        searchSubComponent = null
    }

    private var videosComponent: VideosSubComponent? = null
    fun createVideosComponent(): VideosSubComponent {
        videosComponent = appComponent.plus(VideosModule())
        return videosComponent!!
    }

    fun releaseVideosComponent() {
        videosComponent = null
    }
}