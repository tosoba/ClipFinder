package com.example.there.findclips

import android.app.Application
import com.example.there.findclips.di.AppComponent
import com.example.there.findclips.di.DaggerAppComponent
import com.example.there.findclips.di.category.CategoryModule
import com.example.there.findclips.di.category.CategorySubComponent
import com.example.there.findclips.di.dashboard.DashboardModule
import com.example.there.findclips.di.dashboard.DashboardSubComponent
import com.example.there.findclips.di.favourites.FavouritesModule
import com.example.there.findclips.di.favourites.FavouritesSubComponent
import com.example.there.findclips.di.modules.*
import com.example.there.findclips.di.spotifysearch.SpotifySearchModule
import com.example.there.findclips.di.spotifysearch.SpotifySearchSubComponent
import com.example.there.findclips.di.videossearch.VideosSearchModule
import com.example.there.findclips.di.videossearch.VideosSearchSubComponent
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
                .networkModule(NetworkModule(spotifyApiBaseUrl = getString(R.string.spotify_base_url),
                        accessTokenBaseUrl = getString(R.string.access_token_base_url),
                        spotifyChartsBaseUrl = getString(R.string.spotify_charts_base_url),
                        youtubeBaseUrl = getString(R.string.youtube_base_url)))
                .commonUseCasesModule(CommonUseCasesModule())
                .build()
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

    private var spotifySearchSubComponent: SpotifySearchSubComponent? = null
    fun createSpotifySearchComponent(): SpotifySearchSubComponent {
        spotifySearchSubComponent = appComponent.plus(SpotifySearchModule())
        return spotifySearchSubComponent!!
    }

    fun releaseSpotifySearchComponent() {
        spotifySearchSubComponent = null
    }

    private var videosSearchSubComponent: VideosSearchSubComponent? = null
    fun createVideosSearchComponent(): VideosSearchSubComponent {
        videosSearchSubComponent = appComponent.plus(VideosSearchModule())
        return videosSearchSubComponent!!
    }

    fun releaseVideosSearchComponent() {
        videosSearchSubComponent = null
    }

    private var categorySubComponent: CategorySubComponent? = null
    fun createCategoryComponent(): CategorySubComponent {
        categorySubComponent = appComponent.plus(CategoryModule())
        return categorySubComponent!!
    }

    fun releaseCategoryComponent() {
        categorySubComponent = null
    }
}