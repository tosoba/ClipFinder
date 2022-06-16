package com.clipfinder.core.android.spotify.ext

import androidx.fragment.app.Fragment
import com.clipfinder.core.android.spotify.base.SpotifyAuthController
import com.clipfinder.core.android.spotify.base.SpotifyPlayerController
import com.clipfinder.core.android.util.ext.mainContentFragment
import com.clipfinder.core.ext.castAs

fun Fragment.enableSpotifyPlayButton(playClicked: SpotifyPlayerController.() -> Unit) {
    mainContentFragment?.enablePlayButton {
        val playerController = activity?.castAs<SpotifyPlayerController>()
        if (playerController?.isPlayerLoggedIn == true) {
            playerController.playClicked()
        } else {
            activity?.castAs<SpotifyAuthController>()?.let {
                it.showLoginDialog()
                it.onLoginSuccessful = { playerController?.playClicked() }
            }
        }
    }
}
