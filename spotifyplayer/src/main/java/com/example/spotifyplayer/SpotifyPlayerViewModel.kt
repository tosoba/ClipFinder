package com.example.spotifyplayer

import com.example.coreandroid.base.vm.BaseViewModel

class SpotifyPlayerViewModel() : BaseViewModel() {
    val viewState = SpotifyPlayerViewState()
    val playerState = SpotifyPlayerState()
}