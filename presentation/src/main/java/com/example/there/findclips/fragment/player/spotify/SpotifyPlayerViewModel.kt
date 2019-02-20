package com.example.there.findclips.fragment.player.spotify

import com.example.there.findclips.base.vm.BaseViewModel
import javax.inject.Inject

class SpotifyPlayerViewModel @Inject constructor() : BaseViewModel() {
    val viewState = SpotifyPlayerViewState()
}