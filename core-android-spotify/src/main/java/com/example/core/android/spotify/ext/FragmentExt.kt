package com.example.core.android.spotify.ext

import androidx.fragment.app.Fragment
import com.example.core.android.spotify.controller.SpotifyAuthController
import com.example.core.android.spotify.controller.SpotifyPlayerController
import com.example.core.android.util.ext.mainContentFragment
import com.example.core.ext.castAs

val Fragment.spotifyAuthController: SpotifyAuthController?
    get() = activity as? SpotifyAuthController

fun Fragment.enableSpotifyPlayButton(playClicked: SpotifyPlayerController.() -> Unit) {
    mainContentFragment?.enablePlayButton {
        val playerController = activity?.castAs<SpotifyPlayerController>()
        if (playerController?.isPlayerLoggedIn == true) playerController.playClicked()
        else activity?.castAs<SpotifyAuthController>()?.let {
            it.showLoginDialog()
            it.onLoginSuccessful = { playerController?.playClicked() }
        }
    }
}
