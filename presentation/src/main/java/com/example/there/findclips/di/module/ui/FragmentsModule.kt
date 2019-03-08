package com.example.there.findclips.di.module.ui

import com.example.there.findclips.soundcloud.dashboard.SoundCloudDashboardFragment
import com.example.there.findclips.spotify.account.playlists.AccountPlaylistsFragment
import com.example.there.findclips.spotify.account.saved.AccountSavedFragment
import com.example.there.findclips.spotify.account.top.AccountTopFragment
import com.example.there.findclips.spotify.dashboard.SpotifyDashboardFragment
import com.example.there.findclips.spotify.favourites.spotify.SpotifyFavouritesFragment
import com.example.there.findclips.spotify.player.SpotifyPlayerFragment
import com.example.there.findclips.spotify.search.spotify.SpotifySearchFragment
import com.example.there.findclips.spotify.spotifyitem.album.AlbumFragment
import com.example.there.findclips.spotify.spotifyitem.artist.ArtistFragment
import com.example.there.findclips.spotify.spotifyitem.category.CategoryFragment
import com.example.there.findclips.spotify.spotifyitem.playlist.PlaylistFragment
import com.example.there.findclips.spotify.spotifyitem.track.TrackFragment
import com.example.there.findclips.spotify.trackvideos.TrackVideosFragment
import com.example.there.findclips.videos.favourites.VideosFavouritesFragment
import com.example.there.findclips.videos.player.YoutubePlayerFragment
import com.example.there.findclips.videos.relatedvideos.RelatedVideosFragment
import com.example.there.findclips.videos.search.VideosSearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentsModule {

    @ContributesAndroidInjector
    abstract fun dashboardFragment(): SpotifyDashboardFragment

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

    @ContributesAndroidInjector
    abstract fun spotifyPlayerFragment(): SpotifyPlayerFragment

    @ContributesAndroidInjector
    abstract fun youtubePlayerFragment(): YoutubePlayerFragment

    @ContributesAndroidInjector
    abstract fun relatedVideosFragment(): RelatedVideosFragment

    @ContributesAndroidInjector
    abstract fun soundCloudDashboardFragment(): SoundCloudDashboardFragment
}