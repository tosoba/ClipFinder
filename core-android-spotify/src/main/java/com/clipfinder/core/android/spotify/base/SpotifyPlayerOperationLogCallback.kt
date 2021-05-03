package com.clipfinder.core.android.spotify.base

import com.spotify.sdk.android.player.Error
import com.spotify.sdk.android.player.Player
import timber.log.Timber

object SpotifyPlayerOperationLogCallback : Player.OperationCallback {
    override fun onSuccess() {
        Timber.tag("PLAYER_OP").e("Success")
    }

    override fun onError(error: Error?) {
        Timber.tag("PLAYER_OP").e("Error: ${error?.name ?: "unknown"}")
    }
}
