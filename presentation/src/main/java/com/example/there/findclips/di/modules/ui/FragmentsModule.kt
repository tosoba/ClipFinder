package com.example.there.findclips.di.modules.ui

import com.example.there.findclips.activities.album.AlbumFragment
import com.example.there.findclips.activities.artist.ArtistFragment
import com.example.there.findclips.activities.category.CategoryFragment
import com.example.there.findclips.activities.playlist.PlaylistFragment
import com.example.there.findclips.activities.trackvideos.TrackVideosFragment
import com.example.there.findclips.fragments.dashboard.DashboardFragment
import com.example.there.findclips.fragments.favourites.spotify.SpotifyFavouritesFragment
import com.example.there.findclips.fragments.favourites.videos.VideosFavouritesFragment
import com.example.there.findclips.fragments.search.spotify.SpotifySearchFragment
import com.example.there.findclips.fragments.search.videos.VideosSearchFragment
import com.example.there.findclips.fragments.track.TrackFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentsModule {
    @ContributesAndroidInjector
    abstract fun dashboardFragment(): DashboardFragment

    @ContributesAndroidInjector
    abstract fun spotifyFavouritesFragment(): SpotifyFavouritesFragment

    @ContributesAndroidInjector
    abstract fun videosFavouritesFragment(): VideosFavouritesFragment

    @ContributesAndroidInjector
    abstract fun spotifySearchFragment(): SpotifySearchFragment

    @ContributesAndroidInjector
    abstract fun videosSearchFragment(): VideosSearchFragment

    @ContributesAndroidInjector
    abstract fun trackSearchFragment(): TrackFragment


    @ContributesAndroidInjector
    abstract fun albumFragment(): AlbumFragment

    @ContributesAndroidInjector
    abstract fun artistFragment(): ArtistFragment

    @ContributesAndroidInjector
    abstract fun categoryFragment(): CategoryFragment

    @ContributesAndroidInjector
    abstract fun playlistFragment(): PlaylistFragment

    @ContributesAndroidInjector
    abstract fun trackVideosFragment(): TrackVideosFragment
}