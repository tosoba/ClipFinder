package com.example.there.findclips.di.module.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.findclips.di.vm.ViewModelFactory
import com.example.there.findclips.di.vm.ViewModelKey
import com.example.there.findclips.main.MainViewModel
import com.example.there.findclips.soundcloud.dashboard.SoundCloudDashboardViewModel
import com.example.there.findclips.spotify.account.playlists.AccountPlaylistsViewModel
import com.example.there.findclips.spotify.account.saved.AccountSavedViewModel
import com.example.there.findclips.spotify.account.top.AccountTopViewModel
import com.example.there.findclips.spotify.dashboard.SpotifyDashboardViewModel
import com.example.there.findclips.spotify.favourites.spotify.SpotifyFavouritesViewModel
import com.example.there.findclips.spotify.player.SpotifyPlayerViewModel
import com.example.there.findclips.spotify.search.spotify.SpotifySearchViewModel
import com.example.there.findclips.spotify.spotifyitem.album.AlbumViewModel
import com.example.there.findclips.spotify.spotifyitem.artist.ArtistViewModel
import com.example.there.findclips.spotify.spotifyitem.category.CategoryViewModel
import com.example.there.findclips.spotify.spotifyitem.playlist.PlaylistViewModel
import com.example.there.findclips.spotify.spotifyitem.track.TrackViewModel
import com.example.there.findclips.spotify.trackvideos.TrackVideosViewModel
import com.example.there.findclips.videos.favourites.VideosFavouritesViewModel
import com.example.there.findclips.videos.player.YoutubePlayerViewModel
import com.example.there.findclips.videos.relatedvideos.RelatedVideosViewModel
import com.example.there.findclips.videos.search.VideosSearchViewModel
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
}