package com.example.there.findclips

import android.app.Application
import com.example.there.findclips.di.AppComponent
import com.example.there.findclips.di.DaggerAppComponent
import com.example.there.findclips.di.album.AlbumModule
import com.example.there.findclips.di.album.AlbumSubComponent
import com.example.there.findclips.di.artist.ArtistModule
import com.example.there.findclips.di.artist.ArtistSubComponent
import com.example.there.findclips.di.category.CategoryModule
import com.example.there.findclips.di.category.CategorySubComponent
import com.example.there.findclips.di.dashboard.DashboardModule
import com.example.there.findclips.di.dashboard.DashboardSubComponent
import com.example.there.findclips.di.favourites.FavouritesModule
import com.example.there.findclips.di.favourites.FavouritesSubComponent
import com.example.there.findclips.di.modules.*
import com.example.there.findclips.di.playlist.PlaylistModule
import com.example.there.findclips.di.playlist.PlaylistSubComponent
import com.example.there.findclips.di.relatedvideos.RelatedVideosModule
import com.example.there.findclips.di.relatedvideos.RelatedVideosSubComponent
import com.example.there.findclips.di.spotifysearch.SpotifySearchModule
import com.example.there.findclips.di.spotifysearch.SpotifySearchSubComponent
import com.example.there.findclips.di.track.TrackModule
import com.example.there.findclips.di.track.TrackSubComponent
import com.example.there.findclips.di.videossearch.VideosSearchModule
import com.example.there.findclips.di.videossearch.VideosSearchSubComponent
import com.squareup.leakcanary.LeakCanary


class FindClipsApp : Application() {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

//        initLeakCanary()

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

    private var playlistSubComponent: PlaylistSubComponent? = null
    fun createPlaylistComponent(): PlaylistSubComponent {
        playlistSubComponent = appComponent.plus(PlaylistModule())
        return playlistSubComponent!!
    }

    fun releasePlaylistComponent() {
        playlistSubComponent = null
    }

    private var relatedVideosSubComponent: RelatedVideosSubComponent? = null
    fun createRelatedVideosSubComponent(): RelatedVideosSubComponent {
        relatedVideosSubComponent = appComponent.plus(RelatedVideosModule())
        return relatedVideosSubComponent!!
    }

    fun releaseRelatedVideosSubComponent() {
        relatedVideosSubComponent = null
    }

    private var trackSubComponent: TrackSubComponent? = null
    fun createTrackSubComponent(): TrackSubComponent {
        trackSubComponent = appComponent.plus(TrackModule())
        return trackSubComponent!!
    }

    fun releaseTrackSubComponent() {
        trackSubComponent = null
    }

    private var artistSubComponent: ArtistSubComponent? = null
    fun createArtistSubComponent(): ArtistSubComponent {
        artistSubComponent = appComponent.plus(ArtistModule())
        return artistSubComponent!!
    }

    fun releaseArtistSubComponent() {
        artistSubComponent = null
    }

    private var albumSubComponent: AlbumSubComponent? = null
    fun createAlbumSubComponent(): AlbumSubComponent {
        albumSubComponent = appComponent.plus(AlbumModule())
        return albumSubComponent!!
    }

    fun releaseAlbumSubComponent() {
        albumSubComponent = null
    }
}