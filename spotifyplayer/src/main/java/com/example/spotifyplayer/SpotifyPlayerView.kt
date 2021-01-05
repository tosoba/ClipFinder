package com.example.spotifyplayer

import android.view.View
import android.widget.SeekBar
import com.airbnb.mvrx.MvRxState
import com.example.core.android.spotify.model.Album
import com.example.core.android.spotify.model.Playlist
import com.example.core.android.spotify.model.Track
import com.spotify.sdk.android.player.Metadata
import com.spotify.sdk.android.player.PlaybackState

class SpotifyPlayerView(
    val onSpotifyPlayPauseBtnClickListener: View.OnClickListener,
    val onCloseSpotifyPlayerBtnClickListener: View.OnClickListener,
    val onPreviousBtnClickListener: View.OnClickListener,
    val onNextBtnClickListener: View.OnClickListener,
    val onPlaybackSeekBarProgressChangeListener: SeekBar.OnSeekBarChangeListener
)

data class SpotifyPlayerState(
    val lastPlayedItem: LastPlayedItem = NoLastPlayedItem,
    val playerMetadata: Metadata? = null,
    val playbackState: PlaybackState? = null,
    val backgroundPlaybackNotificationIsShowing: Boolean = false
) : MvRxState

sealed class LastPlayedItem
object NoLastPlayedItem : LastPlayedItem()
data class LastPlayedTrack(val track: Track) : LastPlayedItem()
data class LastPlayedAlbum(val album: Album) : LastPlayedItem()
data class LastPlayedPlaylist(val playlist: Playlist) : LastPlayedItem()
