package com.example.there.findclips.fragment.player.spotify

import android.databinding.ObservableField
import android.view.View
import android.widget.SeekBar

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