package com.example.spotifyplayer

import com.example.coreandroid.base.vm.BaseViewModel
import javax.inject.Inject

class SpotifyPlayerViewModel @Inject constructor() : BaseViewModel() {
    val viewState = SpotifyPlayerViewState()
    val playerState = SpotifyPlayerState()
}