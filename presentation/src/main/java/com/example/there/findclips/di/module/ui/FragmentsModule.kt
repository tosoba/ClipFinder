package com.example.there.findclips.di.module.ui

import com.example.there.findclips.fragment.account.playlists.AccountPlaylistsFragment
import com.example.there.findclips.fragment.account.saved.AccountSavedFragment
import com.example.there.findclips.fragment.account.top.AccountTopFragment
import com.example.there.findclips.fragment.spotifyitem.album.AlbumFragment
import com.example.there.findclips.fragment.spotifyitem.artist.ArtistFragment
import com.example.there.findclips.fragment.spotifyitem.category.CategoryFragment
import com.example.there.findclips.fragment.dashboard.DashboardFragment
import com.example.there.findclips.fragment.favourites.spotify.SpotifyFavouritesFragment
import com.example.there.findclips.fragment.favourites.videos.VideosFavouritesFragment
import com.example.there.findclips.fragment.spotifyitem.playlist.PlaylistFragment
import com.example.there.findclips.fragment.search.spotify.SpotifySearchFragment
import com.example.there.findclips.fragment.search.videos.VideosSearchFragment
import com.example.there.findclips.fragment.spotifyitem.track.TrackFragment
import com.example.there.findclips.fragment.trackvideos.TrackVideosFragment
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

    @ContributesAndroidInjector
    abstract fun accountPlaylistsFragment(): AccountPlaylistsFragment

    @ContributesAndroidInjector
    abstract fun accountTopFragment(): AccountTopFragment

    @ContributesAndroidInjector
    abstract fun accountSavedFragment(): AccountSavedFragment
}