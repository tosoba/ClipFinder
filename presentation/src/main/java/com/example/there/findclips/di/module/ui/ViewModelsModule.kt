package com.example.there.findclips.di.module.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.main.MainViewModel
import com.example.there.findclips.soundcloud.dashboard.SoundCloudDashboardViewModel
import com.example.there.findclips.soundcloud.playlist.SoundCloudPlaylistViewModel
import com.example.there.findclips.soundcloud.trackvideos.SoundCloudTrackVideosViewModel
import com.example.spotifyaccount.playlist.AccountPlaylistsViewModel
import com.example.spotifyaccount.saved.AccountSavedViewModel
import com.example.spotifyaccount.top.AccountTopViewModel
import com.example.spotifydashboard.SpotifyDashboardViewModel
import com.example.spotifyfavourites.spotify.SpotifyFavouritesViewModel
import com.example.spotifyplayer.SpotifyPlayerViewModel
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
    @ViewModelKey(com.example.main.MainViewModel::class)
    abstract fun mainViewModel(viewModel: com.example.main.MainViewModel): ViewModel

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
    @ViewModelKey(com.example.spotifydashboard.SpotifyDashboardViewModel::class)
    abstract fun dashboardViewModel(viewModel: com.example.spotifydashboard.SpotifyDashboardViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(com.example.spotifyfavourites.spotify.SpotifyFavouritesViewModel::class)
    abstract fun spotifyFavouritesViewModel(viewModel: com.example.spotifyfavourites.spotify.SpotifyFavouritesViewModel): ViewModel

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