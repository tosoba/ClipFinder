package com.example.spotifyplayer

import javax.inject.Inject

class SpotifyPlayerViewModel @Inject constructor() : com.example.coreandroid.base.vm.BaseViewModel() {
    val viewState = SpotifyPlayerViewState()
    val playerState = SpotifyPlayerState()
}