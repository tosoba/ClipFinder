package com.example.there.findclips.di.module.ui

import com.example.itemlist.soundcloud.SoundCloudTracksFragment
import com.example.itemlist.spotify.*
import com.example.main.soundcloud.SoundCloudMainFragment
import com.example.main.spotify.SpotifyMainFragment
import com.example.soundclouddashboard.SoundCloudDashboardFragment
import com.example.soundcloudplaylist.SoundCloudPlaylistFragment
import com.example.soundcloudtrackvideos.SoundCloudTrackVideosFragment
import com.example.spotifyaccount.playlist.AccountPlaylistsFragment
import com.example.spotifyaccount.saved.AccountSavedFragment
import com.example.spotifyaccount.top.AccountTopFragment
import com.example.spotifyalbum.AlbumFragment
import com.example.spotifyartist.ArtistFragment
import com.example.spotifycategory.CategoryFragment
import com.example.spotifydashboard.SpotifyDashboardFragment
import com.example.spotifyfavourites.SpotifyFavouritesMainFragment
import com.example.spotifyfavourites.spotify.SpotifyFavouritesFragment
import com.example.spotifyplayer.SpotifyPlayerFragment
import com.example.spotifyplaylist.PlaylistFragment
import com.example.spotifysearch.SpotifySearchMainFragment
import com.example.spotifysearch.spotify.SpotifySearchFragment
import com.example.spotifytrack.TrackFragment
import com.example.spotifytrackvideos.TrackVideosFragment
import com.example.youtubefavourites.VideosFavouritesFragment
import com.example.youtubeplayer.YoutubePlayerFragment
import com.example.youtuberelatedvideos.RelatedVideosFragment
import com.example.youtubesearch.VideosSearchFragment
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

    @ContributesAndroidInjector
    abstract fun soundCloudPlaylistFragment(): SoundCloudPlaylistFragment

    @ContributesAndroidInjector
    abstract fun soundCloudTrackVideosFragment(): SoundCloudTrackVideosFragment

    @ContributesAndroidInjector
    abstract fun spotifyAlbumsFragment(): SpotifyAlbumsFragment

    @ContributesAndroidInjector
    abstract fun spotifyAristsFragment(): SpotifyArtistsFragment

    @ContributesAndroidInjector
    abstract fun spotifyCategoriesFragment(): SpotifyCategoriesFragment

    @ContributesAndroidInjector
    abstract fun spotifyPlaylistsFragment(): SpotifyPlaylistsFragment

    @ContributesAndroidInjector
    abstract fun spotifyTracksFragment(): SpotifyTracksFragment

    @ContributesAndroidInjector
    abstract fun soundCloudTracksFragment(): SoundCloudTracksFragment

    @ContributesAndroidInjector
    abstract fun spotifyMainFragment(): SpotifyMainFragment

    @ContributesAndroidInjector
    abstract fun soundCloudMainFragment(): SoundCloudMainFragment

    @ContributesAndroidInjector
    abstract fun spotifyFavouritesMainFragment(): SpotifyFavouritesMainFragment

    @ContributesAndroidInjector
    abstract fun spotifySearchMainFragment(): SpotifySearchMainFragment
}