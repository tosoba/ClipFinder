package com.clipfinder.spotifyplayer

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.clipfinder.core.android.base.viewmodel.MvRxViewModel
import com.clipfinder.core.android.spotify.model.Album
import com.clipfinder.core.android.spotify.model.Playlist
import com.clipfinder.core.android.spotify.model.Track
import com.spotify.sdk.android.player.Metadata
import com.spotify.sdk.android.player.PlaybackState

class SpotifyPlayerViewModel(
    initialState: SpotifyPlayerState
) : MvRxViewModel<SpotifyPlayerState>(initialState) {

    fun updatePlayerNotificationState(isShowing: Boolean) = setState {
        copy(backgroundPlaybackNotificationIsShowing = isShowing)
    }

    fun onPlaybackEvent(playerMetadata: Metadata?, playbackState: PlaybackState?) {
        setState { copy(playerMetadata = playerMetadata, playbackState = playbackState) }
    }

    fun onLoadTrack(track: Track) = setState { copy(lastPlayedItem = LastPlayedTrack(track)) }
    fun onLoadAlbum(album: Album) = setState { copy(lastPlayedItem = LastPlayedAlbum(album)) }
    fun onLoadPlaylist(playlist: Playlist) = setState { copy(lastPlayedItem = LastPlayedPlaylist(playlist)) }

    companion object : MvRxViewModelFactory<SpotifyPlayerViewModel, SpotifyPlayerState> {
        override fun create(
            viewModelContext: ViewModelContext, state: SpotifyPlayerState
        ): SpotifyPlayerViewModel = SpotifyPlayerViewModel(state)
    }
}
