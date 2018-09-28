package com.example.there.findclips.di.module.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.findclips.di.vm.ViewModelFactory
import com.example.there.findclips.di.vm.ViewModelKey
import com.example.there.findclips.fragment.account.playlists.AccountPlaylistsViewModel
import com.example.there.findclips.fragment.account.saved.AccountSavedViewModel
import com.example.there.findclips.fragment.account.top.AccountTopViewModel
import com.example.there.findclips.fragment.spotifyitem.album.AlbumViewModel
import com.example.there.findclips.fragment.spotifyitem.artist.ArtistViewModel
import com.example.there.findclips.fragment.spotifyitem.category.CategoryViewModel
import com.example.there.findclips.fragment.dashboard.DashboardViewModel
import com.example.there.findclips.fragment.favourites.spotify.SpotifyFavouritesViewModel
import com.example.there.findclips.fragment.favourites.videos.VideosFavouritesViewModel
import com.example.there.findclips.fragment.spotifyitem.playlist.PlaylistViewModel
import com.example.there.findclips.fragment.search.spotify.SpotifySearchViewModel
import com.example.there.findclips.fragment.search.videos.VideosSearchViewModel
import com.example.there.findclips.fragment.spotifyitem.track.TrackViewModel
import com.example.there.findclips.fragment.trackvideos.TrackVideosViewModel
import com.example.there.findclips.main.MainViewModel
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
    @ViewModelKey(DashboardViewModel::class)
    abstract fun dashboardViewModel(viewModel: DashboardViewModel): ViewModel

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
}