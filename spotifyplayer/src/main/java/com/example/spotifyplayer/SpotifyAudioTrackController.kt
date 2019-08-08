package com.example.spotifyplayer

import android.annotation.TargetApi
import android.media.AudioTrack
import android.os.Build
import com.spotify.sdk.android.player.AudioController
import com.spotify.sdk.android.player.AudioRingBuffer
import java.util.concurrent.Executors
import java.util.concurrent.RejectedExecutionException

class SpotifyAudioTrackController : AudioController {
    companion object {
        private const val AUDIO_BUFFER_SIZE_SAMPLES = 4096
        private const val AUDIO_BUFFER_CAPACITY = 81920
    }

    val mAudioBuffer = AudioRingBuffer(AUDIO_BUFFER_CAPACITY)
    private val mExecutorService = Executors.newSingleThreadExecutor()
    private val mPlayingMutex = Any()
    private var mAudioTrack: AudioTrack? = null
    private var mSampleRate: Int = 0
    private var mChannels: Int = 0
    private val mAudioRunnable = object : Runnable {
        val pendingSamples = ShortArray(AUDIO_BUFFER_SIZE_SAMPLES)

        override fun run() {
            val itemsRead = this@SpotifyAudioTrackController.mAudioBuffer.peek(pendingSamples)
            if (itemsRead > 0) {
                val itemsWritten = this@SpotifyAudioTrackController.writeSamplesToAudioOutput(pendingSamples, itemsRead)
                this@SpotifyAudioTrackController.mAudioBuffer.remove(itemsWritten)
            }
        }
    }

    override fun onAudioDataDelivered(samples: ShortArray, sampleCount: Int, sampleRate: Int, channels: Int): Int {
        if (mAudioTrack != null && (mSampleRate != sampleRate || mChannels != channels)) {
            synchronized(mPlayingMutex) {
                mAudioTrack?.release()
                mAudioTrack = null
            }
        }

        mSampleRate = sampleRate
        mChannels = channels
        if (mAudioTrack == null) {
            createAudioTrack(sampleRate, channels)
        }

        try {
            mExecutorService.execute(mAudioRunnable)
        } catch (var7: RejectedExecutionException) {
        }

        return mAudioBuffer.write(samples, sampleCount)
    }

    override fun onAudioFlush() {
        mAudioBuffer.clear()
        if (mAudioTrack != null) {
            synchronized(mPlayingMutex) {
                mAudioTrack?.pause()
                mAudioTrack?.flush()
                mAudioTrack?.release()
                mAudioTrack = null
            }
        }
    }

    override fun onAudioPaused() {
        mAudioTrack?.pause()
    }

    override fun onAudioResumed() {
        mAudioTrack?.play()
    }

    override fun start() {}

    override fun stop() {
        mExecutorService.shutdown()
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
        synchronized(mPlayingMutex) {
            mAudioTrack = AudioTrack(3, sampleRate, channelConfig.toInt(), 2, bufferSize, 1)
            if (mAudioTrack?.state == 1) {
                if (Build.VERSION.SDK_INT >= 21) {
                    mAudioTrack?.setVolume(maxVolume)
                } else {
                    mAudioTrack?.setStereoVolume(maxVolume, maxVolume)
                }

                mAudioTrack?.play()
            } else {
                mAudioTrack?.release()
                mAudioTrack = null
            }
        }
    }

    private fun writeSamplesToAudioOutput(samples: ShortArray, samplesCount: Int): Int {
        if (isAudioTrackPlaying()) {
            val itemsWritten = mAudioTrack?.write(samples, 0, samplesCount)
            if (itemsWritten != null && itemsWritten > 0) {
                return itemsWritten
            }
        }

        return 0
    }

    private fun isAudioTrackPlaying(): Boolean = mAudioTrack?.playState == 3
}