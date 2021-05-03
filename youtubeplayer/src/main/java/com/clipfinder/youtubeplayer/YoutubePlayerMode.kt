package com.clipfinder.youtubeplayer

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
}
