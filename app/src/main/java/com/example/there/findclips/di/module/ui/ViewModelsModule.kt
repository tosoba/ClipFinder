package com.example.there.findclips.di.module.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coreandroid.di.vm.ViewModelFactory
import com.example.coreandroid.di.vm.ViewModelKey
import com.example.main.MainViewModel
import com.example.soundclouddashboard.SoundCloudDashboardViewModel
import com.example.soundcloudplaylist.SoundCloudPlaylistViewModel
import com.example.soundcloudtrackvideos.SoundCloudTrackVideosViewModel
import com.example.spotifyaccount.playlist.AccountPlaylistsViewModel
import com.example.spotifyaccount.saved.AccountSavedViewModel
import com.example.spotifyaccount.top.AccountTopViewModel
import com.example.spotifyalbum.AlbumViewModel
import com.example.spotifyartist.ArtistViewModel
import com.example.spotifycategory.CategoryViewModel
import com.example.spotifydashboard.SpotifyDashboardViewModel
import com.example.spotifyfavourites.spotify.SpotifyFavouritesViewModel
import com.example.spotifyplayer.SpotifyPlayerViewModel
import com.example.spotifyplaylist.PlaylistViewModel
import com.example.spotifysearch.spotify.SpotifySearchViewModel
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
    @ViewModelKey(MainViewModel::class)
    abstract fun mainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AlbumViewModel::class)
    abstract fun albumViewModel(viewModel: AlbumViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ArtistViewModel::class)
    abstract fun artistViewModel(viewModel: ArtistViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoryViewModel::class)
    abstract fun categoryViewModel(viewModel: CategoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlaylistViewModel::class)
    abstract fun playlistViewModel(viewModel: PlaylistViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TrackVideosViewModel::class)
    abstract fun trackVideosViewModel(viewModel: TrackVideosViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SpotifyDashboardViewModel::class)
    abstract fun dashboardViewModel(viewModel: SpotifyDashboardViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SpotifyFavouritesViewModel::class)
    abstract fun spotifyFavouritesViewModel(viewModel: SpotifyFavouritesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VideosFavouritesViewModel::class)
    abstract fun videosFavouritesViewModel(viewModel: VideosFavouritesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SpotifySearchViewModel::class)
    abstract fun spotifySearchViewModel(viewModel: SpotifySearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VideosSearchViewModel::class)
    abstract fun videosSearchViewModel(viewModel: VideosSearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TrackViewModel::class)
    abstract fun trackViewModel(viewModel: TrackViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AccountPlaylistsViewModel::class)
    abstract fun accountPlaylistsViewModel(viewModel: AccountPlaylistsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AccountTopViewModel::class)
    abstract fun accountTopViewModel(viewModel: AccountTopViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AccountSavedViewModel::class)
    abstract fun accountSavedViewModel(viewModel: AccountSavedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SpotifyPlayerViewModel::class)
    abstract fun spotifyPlayerViewModel(viewModel: SpotifyPlayerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RelatedVideosViewModel::class)
    abstract fun relatedVideosViewModel(viewModel: RelatedVideosViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(YoutubePlayerViewModel::class)
    abstract fun youtubePlayerViewModel(viewModel: YoutubePlayerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SoundCloudDashboardViewModel::class)
    abstract fun soundCloudDashboardViewModel(viewModel: SoundCloudDashboardViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SoundCloudPlaylistViewModel::class)
    abstract fun soundCloudPlaylistViewModel(viewModel: SoundCloudPlaylistViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SoundCloudTrackVideosViewModel::class)
    abstract fun soundCloudTrackVideosViewModel(viewModel: SoundCloudTrackVideosViewModel): ViewModel
}