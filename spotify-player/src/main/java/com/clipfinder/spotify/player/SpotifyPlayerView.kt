package com.clipfinder.spotify.player

import android.view.View
import android.widget.SeekBar

class SpotifyPlayerView(
    val onSpotifyPlayPauseBtnClickListener: View.OnClickListener,
    val onCloseSpotifyPlayerBtnClickListener: View.OnClickListener,
    val onPreviousBtnClickListener: View.OnClickListener,
    val onNextBtnClickListener: View.OnClickListener,
    val onPlaybackSeekBarProgressChangeListener: SeekBar.OnSeekBarChangeListener
)
