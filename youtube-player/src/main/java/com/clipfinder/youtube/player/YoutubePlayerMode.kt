package com.clipfinder.youtube.player

import com.clipfinder.core.android.model.videos.Video
import com.clipfinder.core.android.model.videos.VideoPlaylist

sealed class YoutubePlayerMode {
    object Idle : YoutubePlayerMode()
    data class SingleVideo(val video: Video) : YoutubePlayerMode()
    data class Playlist(
        val playlist: VideoPlaylist,
        val videos: List<Video>,
        val currentVideoIndex: Int
    ) : YoutubePlayerMode()

    val prevTrackAvailable: Boolean
        get() = this is Playlist && currentVideoIndex > 0

    val nextTrackAvailable: Boolean
        get() = this is Playlist && currentVideoIndex < videos.size - 1
}
