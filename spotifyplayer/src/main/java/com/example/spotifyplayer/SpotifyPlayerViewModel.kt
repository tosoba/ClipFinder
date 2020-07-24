package com.example.spotifyplayer

import com.example.core.android.base.vm.BaseViewModel

class SpotifyPlayerViewModel : BaseViewModel() {
    val viewState = SpotifyPlayerViewState()
    val playerState = SpotifyPlayerState()
}
