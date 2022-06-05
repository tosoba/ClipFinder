package com.clipfinder.core.android.spotify.visualizer.datasource

import com.clipfinder.core.android.spotify.player.SpotifyAudioTrackController
import me.bogerchan.niervisualizer.NierVisualizerManager
import kotlin.math.pow

class SpotifyPlayerNVDataSource(
    private val audioTrackController: SpotifyAudioTrackController,
) : NierVisualizerManager.NVDataSource {
    private val audioBufferSize = 81920
    private val audioRecordByteBuffer by
        lazy(LazyThreadSafetyMode.NONE) { ByteArray(audioBufferSize / 2) }
    private val audioRecordShortBuffer by
        lazy(LazyThreadSafetyMode.NONE) { ShortArray(audioBufferSize / 2) }
    private val outputBuffer: ByteArray = ByteArray(512)

    override fun getDataSamplingInterval() = 0L
    override fun getDataLength() = outputBuffer.size
    override fun fetchWaveData(): ByteArray? = null
    override fun fetchFftData(): ByteArray {
        audioTrackController.audioBuffer.peek(audioRecordShortBuffer)
        audioRecordShortBuffer.forEachIndexed { index, sh ->
            audioRecordByteBuffer[index] = (sh / 2.0.pow(10.0)).toInt().toByte()
        }
        var bufferIndex = 0
        for (idx in
            audioRecordByteBuffer.indices step (audioRecordByteBuffer.size / (outputBuffer.size))) {
            if (bufferIndex >= outputBuffer.size) break
            outputBuffer[bufferIndex++] = audioRecordByteBuffer[idx]
        }
        return outputBuffer
    }
}
