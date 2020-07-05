package com.example.spotifyplayer

import com.spotify.sdk.android.player.AudioController
import com.spotify.sdk.android.player.Config
import com.spotify.sdk.android.player.PlayerInitializationException
import com.spotify.sdk.android.player.SpotifyPlayer
import java.util.*

object SpotifyPlayerManager {
    private var sPlayer: SpotifyPlayer? = null
    private val sPlayerMutex = Any()
    private val sPlayerReferences = Collections.newSetFromMap<Any>(IdentityHashMap())

    fun getPlayer(
        config: Config,
        reference: Any,
        audioController: AudioController,
        observer: SpotifyPlayer.InitializationObserver
    ): SpotifyPlayer {
        val builder = SpotifyPlayer.Builder(config).setAudioController(audioController)
        return getPlayer(builder, reference, observer)
    }

    private fun getPlayer(
        builder: SpotifyPlayer.Builder,
        reference: Any,
        observer: SpotifyPlayer.InitializationObserver?
    ): SpotifyPlayer = synchronized(sPlayerMutex) {
        sPlayer?.let {
            if (!it.isShutdown) {
                var waitCounter = 300
                while (!it.isInitialized && waitCounter > 0) try {
                    --waitCounter
                    Thread.sleep(10L) //TODO: get rid of this...
                } catch (var7: InterruptedException) {
                    Thread.currentThread().interrupt()
                }
            }

            if (observer != null) when {
                it.isInitialized -> observer.onInitialized(sPlayer)
                it.isShutdown -> observer.onError(PlayerInitializationException("Player already shut down"))
                else -> observer.onError(PlayerInitializationException("Player initialization failed"))
            }
        } ?: run { sPlayer = builder.build(observer) }

        sPlayerReferences.add(reference)
        return sPlayer!!
    }
}
