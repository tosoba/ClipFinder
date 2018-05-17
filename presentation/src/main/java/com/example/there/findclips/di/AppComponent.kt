package com.example.there.findclips.di

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
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    NetworkModule::class,
    DataModule::class,
    CommonUseCasesModule::class
])
interface AppComponent {
    fun plus(dashboardModule: DashboardModule): DashboardSubComponent
    fun plus(favouritesModule: FavouritesModule): FavouritesSubComponent
    fun plus(spotifySearchModule: SpotifySearchModule): SpotifySearchSubComponent
    fun plus(videosSearchModule: VideosSearchModule): VideosSearchSubComponent
    fun plus(categoryModule: CategoryModule): CategorySubComponent
    fun plus(playlistModule: PlaylistModule): PlaylistSubComponent
    fun plus(relatedVideosModule: RelatedVideosModule): RelatedVideosSubComponent
    fun plus(trackModule: TrackModule): TrackSubComponent
    fun plus(artistModule: ArtistModule): ArtistSubComponent
    fun plus(albumModule: AlbumModule): AlbumSubComponent
}