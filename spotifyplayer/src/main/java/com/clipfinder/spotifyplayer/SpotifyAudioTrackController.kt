package com.clipfinder.spotifyplayer

import android.annotation.TargetApi
import android.media.AudioTrack
import android.os.Build
import com.spotify.sdk.android.player.AudioController
import com.spotify.sdk.android.player.AudioRingBuffer
import java.util.concurrent.Executors
import java.util.concurrent.RejectedExecutionException

class SpotifyAudioTrackController : AudioController {
    val audioBuffer = AudioRingBuffer(AUDIO_BUFFER_CAPACITY)
    private val executorService = Executors.newSingleThreadExecutor()
    private val playingMutex = Any()
    private var audioTrack: AudioTrack? = null
    private var sampleRate: Int = 0
    private var channels: Int = 0

    private val audioRunnable = object : Runnable {
        val pendingSamples = ShortArray(AUDIO_BUFFER_SIZE_SAMPLES)

        override fun run() {
            val itemsRead = this@SpotifyAudioTrackController.audioBuffer.peek(pendingSamples)
            if (itemsRead > 0) {
                val itemsWritten = this@SpotifyAudioTrackController.writeSamplesToAudioOutput(pendingSamples, itemsRead)
                this@SpotifyAudioTrackController.audioBuffer.remove(itemsWritten)
            }
        }
    }

    override fun onAudioDataDelivered(samples: ShortArray, sampleCount: Int, sampleRate: Int, channels: Int): Int {
        if (audioTrack != null && (this.sampleRate != sampleRate || this.channels != channels)) {
            synchronized(playingMutex) {
                audioTrack?.release()
                audioTrack = null
            }
        }

        this.sampleRate = sampleRate
        this.channels = channels
        if (audioTrack == null) {
            createAudioTrack(sampleRate, channels)
        }

        try {
            executorService.execute(audioRunnable)
        } catch (var7: RejectedExecutionException) {
        }

        return audioBuffer.write(samples, sampleCount)
    }

    override fun onAudioFlush() {
        audioBuffer.clear()
        if (audioTrack != null) {
            synchronized(playingMutex) {
                audioTrack?.pause()
                audioTrack?.flush()
                audioTrack?.release()
                audioTrack = null
            }
        }
    }

    override fun onAudioPaused() {
        audioTrack?.pause()
    }

    override fun onAudioResumed() {
        audioTrack?.play()
    }

    override fun start() {}

    override fun stop() {
        executorService.shutdown()
    }

    @TargetApi(21)
    private fun createAudioTrack(sampleRate: Int, channels: Int) {
        val channelConfig: Byte = when (channels) {
            0 -> throw IllegalStateException("Input source has 0 channels")
            1 -> 4
            2 -> 12
            else -> throw IllegalArgumentException("Unsupported input source has $channels channels")
        }

        val bufferSize = AudioTrack.getMinBufferSize(sampleRate, channelConfig.toInt(), 2) * 2
        val maxVolume = AudioTrack.getMaxVolume()
        synchronized(playingMutex) {
            audioTrack = AudioTrack(3, sampleRate, channelConfig.toInt(), 2, bufferSize, 1)
            if (audioTrack?.state == 1) {
                if (Build.VERSION.SDK_INT >= 21) {
                    audioTrack?.setVolume(maxVolume)
                } else {
                    audioTrack?.setStereoVolume(maxVolume, maxVolume)
                }

                audioTrack?.play()
            } else {
                audioTrack?.release()
                audioTrack = null
            }
        }
    }

    private fun writeSamplesToAudioOutput(samples: ShortArray, samplesCount: Int): Int {
        if (isAudioTrackPlaying()) {
            val itemsWritten = audioTrack?.write(samples, 0, samplesCount)
            if (itemsWritten != null && itemsWritten > 0) return itemsWritten
        }
        return 0
    }

    private fun isAudioTrackPlaying(): Boolean = audioTrack?.playState == 3

    companion object {
        private const val AUDIO_BUFFER_SIZE_SAMPLES = 4096
        private const val AUDIO_BUFFER_CAPACITY = 81920
    }
}
