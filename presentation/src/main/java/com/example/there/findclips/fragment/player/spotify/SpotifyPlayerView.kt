package com.example.there.findclips.fragment.player.spotify

import android.databinding.ObservableField
import android.view.View
import android.widget.SeekBar
import com.example.there.findclips.model.entity.Album
import com.example.there.findclips.model.entity.Playlist
import com.example.there.findclips.model.entity.Track
import com.spotify.sdk.android.player.Metadata
import com.spotify.sdk.android.player.PlaybackState

class SpotifyPlayerViewState(
        val playbackSeekbarMaxValue: ObservableField<Int> = ObservableField(0),
        val nextTrackExists: ObservableField<Boolean> = ObservableField(false),
        val previousTrackExists: ObservableField<Boolean> = ObservableField(false),
        val currentTrackTitle: ObservableField<String> = ObservableField("")
)

class SpotifyPlayerView(
        val state: SpotifyPlayerViewState,
        val onSpotifyPlayPauseBtnClickListener: View.OnClickListener,
        val onCloseSpotifyPlayerBtnClickListener: View.OnClickListener,
        val onPreviousBtnClickListener: View.OnClickListener,
        val onNextBtnClickListener: View.OnClickListener,
        val onPlaybackSeekBarProgressChangeListener: SeekBar.OnSeekBarChangeListener
)

class SpotifyPlayerState(
        var lastPlayedTrack: Track? = null,
        var lastPlayedAlbum: Album? = null,
        var lastPlayedPlaylist: Playlist? = null,
        var playerMetadata: Metadata? = null,
        var currentPlaybackState: PlaybackState? = null,
        var backgroundPlaybackNotificationIsShowing: Boolean = false
)