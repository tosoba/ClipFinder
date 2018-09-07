package com.example.there.findclips.di.modules.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.findclips.activities.album.AlbumViewModel
import com.example.there.findclips.activities.artist.ArtistViewModel
import com.example.there.findclips.activities.category.CategoryViewModel
import com.example.there.findclips.activities.playlist.PlaylistViewModel
import com.example.there.findclips.activities.trackvideos.TrackVideosViewModel
import com.example.there.findclips.di.vm.ViewModelFactory
import com.example.there.findclips.di.vm.ViewModelKey
import com.example.there.findclips.fragments.dashboard.DashboardViewModel
import com.example.there.findclips.fragments.favourites.spotify.SpotifyFavouritesViewModel
import com.example.there.findclips.fragments.favourites.videos.VideosFavouritesViewModel
import com.example.there.findclips.fragments.search.spotify.SpotifySearchViewModel
import com.example.there.findclips.fragments.search.videos.VideosSearchViewModel
import com.example.there.findclips.fragments.track.TrackViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelsModule {

    @Binds
    abstract fun viewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

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
}