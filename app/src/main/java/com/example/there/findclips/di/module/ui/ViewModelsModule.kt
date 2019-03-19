package com.example.there.findclips.di.module.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.main.MainViewModel
import com.example.soundclouddashboard.SoundCloudDashboardViewModel
import com.example.soundcloudplaylist.SoundCloudPlaylistViewModel
import com.example.soundcloudtrackvideos.SoundCloudTrackVideosViewModel
import com.example.spotifyaccount.playlist.AccountPlaylistsViewModel
import com.example.spotifyaccount.saved.AccountSavedViewModel
import com.example.spotifyaccount.top.AccountTopViewModel
import com.example.spotifydashboard.SpotifyDashboardViewModel
import com.example.spotifyfavourites.spotify.SpotifyFavouritesViewModel
import com.example.spotifyplayer.SpotifyPlayerViewModel
import com.example.spotifysearch.spotify.SpotifySearchViewModel
import com.example.spotifyalbum.AlbumViewModel
import com.example.spotifyartist.ArtistViewModel
import com.example.spotifycategory.CategoryViewModel
import com.example.spotifyplaylist.PlaylistViewModel
import com.example.spotifytrack.TrackViewModel
import com.example.spotifytrackvideos.TrackVideosViewModel
import com.example.youtubefavourites.VideosFavouritesViewModel
import com.example.youtubeplayer.YoutubePlayerViewModel
import com.example.youtuberelatedvideos.RelatedVideosViewModel
import com.example.youtubesearch.VideosSearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelsModule {

    @Binds
    abstract fun viewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(com.example.main.MainViewModel::class)
    abstract fun mainViewModel(viewModel: com.example.main.MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(com.example.spotifyalbum.AlbumViewModel::class)
    abstract fun albumViewModel(viewModel: com.example.spotifyalbum.AlbumViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(com.example.spotifyartist.ArtistViewModel::class)
    abstract fun artistViewModel(viewModel: com.example.spotifyartist.ArtistViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(com.example.spotifycategory.CategoryViewModel::class)
    abstract fun categoryViewModel(viewModel: com.example.spotifycategory.CategoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(com.example.spotifyplaylist.PlaylistViewModel::class)
    abstract fun playlistViewModel(viewModel: com.example.spotifyplaylist.PlaylistViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(com.example.spotifytrackvideos.TrackVideosViewModel::class)
    abstract fun trackVideosViewModel(viewModel: com.example.spotifytrackvideos.TrackVideosViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(com.example.spotifydashboard.SpotifyDashboardViewModel::class)
    abstract fun dashboardViewModel(viewModel: com.example.spotifydashboard.SpotifyDashboardViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(com.example.spotifyfavourites.spotify.SpotifyFavouritesViewModel::class)
    abstract fun spotifyFavouritesViewModel(viewModel: com.example.spotifyfavourites.spotify.SpotifyFavouritesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(com.example.youtubefavourites.VideosFavouritesViewModel::class)
    abstract fun videosFavouritesViewModel(viewModel: com.example.youtubefavourites.VideosFavouritesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(com.example.spotifysearch.spotify.SpotifySearchViewModel::class)
    abstract fun spotifySearchViewModel(viewModel: com.example.spotifysearch.spotify.SpotifySearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(com.example.youtubesearch.VideosSearchViewModel::class)
    abstract fun videosSearchViewModel(viewModel: com.example.youtubesearch.VideosSearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(com.example.spotifytrack.TrackViewModel::class)
    abstract fun trackViewModel(viewModel: com.example.spotifytrack.TrackViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(com.example.spotifyaccount.playlist.AccountPlaylistsViewModel::class)
    abstract fun accountPlaylistsViewModel(viewModel: com.example.spotifyaccount.playlist.AccountPlaylistsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(com.example.spotifyaccount.top.AccountTopViewModel::class)
    abstract fun accountTopViewModel(viewModel: com.example.spotifyaccount.top.AccountTopViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(com.example.spotifyaccount.saved.AccountSavedViewModel::class)
    abstract fun accountSavedViewModel(viewModel: com.example.spotifyaccount.saved.AccountSavedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(com.example.spotifyplayer.SpotifyPlayerViewModel::class)
    abstract fun spotifyPlayerViewModel(viewModel: com.example.spotifyplayer.SpotifyPlayerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(com.example.youtuberelatedvideos.RelatedVideosViewModel::class)
    abstract fun relatedVideosViewModel(viewModel: com.example.youtuberelatedvideos.RelatedVideosViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(com.example.youtubeplayer.YoutubePlayerViewModel::class)
    abstract fun youtubePlayerViewModel(viewModel: com.example.youtubeplayer.YoutubePlayerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(com.example.soundclouddashboard.SoundCloudDashboardViewModel::class)
    abstract fun soundCloudDashboardViewModel(viewModel: com.example.soundclouddashboard.SoundCloudDashboardViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(com.example.soundcloudplaylist.SoundCloudPlaylistViewModel::class)
    abstract fun soundCloudPlaylistViewModel(viewModel: com.example.soundcloudplaylist.SoundCloudPlaylistViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(com.example.soundcloudtrackvideos.SoundCloudTrackVideosViewModel::class)
    abstract fun soundCloudTrackVideosViewModel(viewModel: com.example.soundcloudtrackvideos.SoundCloudTrackVideosViewModel): ViewModel
}