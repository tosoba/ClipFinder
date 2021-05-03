package com.clipfinder.spotify.player

import com.airbnb.mvrx.MvRxState
import com.spotify.sdk.android.player.Metadata
import com.spotify.sdk.android.player.PlaybackState

data class SpotifyPlayerState(
    val lastPlayedItem: LastPlayedItem = NoLastPlayedItem,
    val playerMetadata: Metadata? = null,
    val playbackState: PlaybackState? = null,
    val showingPlaybackNotification: Boolean = false
) : MvRxState
