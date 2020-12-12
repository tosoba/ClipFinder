package com.example.spotifyplayer

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.spotify.model.Album
import com.example.core.android.spotify.model.Playlist
import com.example.core.android.spotify.model.Track
import com.spotify.sdk.android.player.Metadata
import com.spotify.sdk.android.player.PlaybackState

class SpotifyPlayerViewModel(
    initialState: SpotifyPlayerState
) : MvRxViewModel<SpotifyPlayerState>(initialState) {

    fun updatePlayerNotificationState(isShowing: Boolean) = setState {
        copy(backgroundPlaybackNotificationIsShowing = isShowing)
    }

    fun onLoadTrack(track: Track) = setState {
        copy(lastPlayedTrack = track, lastPlayedAlbum = null, lastPlayedPlaylist = null)
    }

    fun onLoadAlbum(album: Album) = setState {
        copy(lastPlayedTrack = null, lastPlayedAlbum = album, lastPlayedPlaylist = null)
    }

    fun onLoadPlaylist(playlist: Playlist) = setState {
        copy(lastPlayedTrack = null, lastPlayedAlbum = null, lastPlayedPlaylist = playlist)
    }

    fun onPlaybackEvent(playerMetadata: Metadata?, playbackState: PlaybackState?) {
        setState { copy(playerMetadata = playerMetadata, playbackState = playbackState) }
    }

    companion object : MvRxViewModelFactory<SpotifyPlayerViewModel, SpotifyPlayerState> {
        override fun create(
            viewModelContext: ViewModelContext, state: SpotifyPlayerState
        ): SpotifyPlayerViewModel = SpotifyPlayerViewModel(state)
    }
}
