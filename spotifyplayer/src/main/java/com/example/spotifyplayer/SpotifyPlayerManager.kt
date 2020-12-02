package com.example.spotifyplayer

import com.spotify.sdk.android.player.AudioController
import com.spotify.sdk.android.player.Config
import com.spotify.sdk.android.player.PlayerInitializationException
import com.spotify.sdk.android.player.SpotifyPlayer
import java.util.*

object SpotifyPlayerManager {
    private var player: SpotifyPlayer? = null
    private val playerMutex = Any()
    private val playerReferences = Collections.newSetFromMap<Any>(IdentityHashMap())

    fun getPlayer(
        config: Config,
        reference: Any,
        audioController: AudioController,
        observer: SpotifyPlayer.InitializationObserver
    ): SpotifyPlayer = getPlayer(
        SpotifyPlayer.Builder(config).setAudioController(audioController),
        reference,
        observer
    )

    private fun getPlayer(
        builder: SpotifyPlayer.Builder,
        reference: Any,
        observer: SpotifyPlayer.InitializationObserver?
    ): SpotifyPlayer = synchronized(playerMutex) {
        player?.let {
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
                it.isInitialized -> observer.onInitialized(player)
                it.isShutdown -> observer.onError(PlayerInitializationException("Player already shut down"))
                else -> observer.onError(PlayerInitializationException("Player initialization failed"))
            }
        } ?: run { player = builder.build(observer) }

        playerReferences.add(reference)
        return player!!
    }
}
