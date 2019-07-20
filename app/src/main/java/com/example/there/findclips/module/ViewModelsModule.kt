package com.example.there.findclips.module

import com.example.main.MainViewModel
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
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { MainViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { AlbumViewModel(get(), get(), get(), get(), get()) }
    viewModel { ArtistViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { CategoryViewModel(get(), get(), get(), get()) }
    viewModel { PlaylistViewModel(get(), get(), get(), get()) }
    viewModel { TrackVideosViewModel(get(), get(), get()) }
    viewModel { SpotifyDashboardViewModel(get(), get(), get(), get()) }
    viewModel { SpotifyFavouritesViewModel(get(), get(), get(), get(), get()) }
    viewModel { VideosFavouritesViewModel(get(), get()) }
    viewModel { SpotifySearchViewModel(get()) }
    viewModel { VideosSearchViewModel(get(), get(), get(), get()) }
    viewModel { TrackViewModel(get(), get(), get(), get()) }
    viewModel { AccountPlaylistsViewModel(get()) }
    viewModel { AccountTopViewModel(get(), get()) }
    viewModel { AccountSavedViewModel(get(), get()) }
    viewModel { SpotifyPlayerViewModel() }
    viewModel { RelatedVideosViewModel(get(), get()) }
    viewModel { YoutubePlayerViewModel() }
    viewModel { SoundCloudPlaylistViewModel(get(), get()) }
    viewModel { SoundCloudTrackVideosViewModel(get(), get(), get(), get()) }
}

