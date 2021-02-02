package com.clipfinder.youtubeplayer

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.clipfinder.core.android.base.viewmodel.MvRxViewModel
import com.clipfinder.core.android.model.videos.Video
import com.clipfinder.core.android.model.videos.VideoPlaylist

private typealias State = YoutubePlayerState

class YoutubePlayerViewModel(initialState: State) : MvRxViewModel<State>(initialState) {
    fun onLoadVideo(video: Video) = setState { copy(lastPlayedVideo = video, lastPlayedVideoPlaylist = null) }

    fun clearLastPlayed() = setState { copy(lastPlayedVideo = null, lastPlayedVideoPlaylist = null) }

    fun updatePlaybackState(inProgress: Boolean) = setState { copy(playbackInProgress = inProgress) }

    fun onNextVideoFromPlaylistStarted() = setState { copy(currentPlaylistVideoIndex = currentPlaylistVideoIndex + 1) }

    fun onLoadVideoPlaylist(videoPlaylist: VideoPlaylist, videos: List<Video>) = setState {
        copy(
            lastPlayedVideoPlaylist = videoPlaylist,
            playlistVideos = videos,
            currentPlaylistVideoIndex = 0,
            lastPlayedVideo = null
        )
    }

    companion object : MvRxViewModelFactory<YoutubePlayerViewModel, State> {
        override fun create(
            viewModelContext: ViewModelContext, state: State
        ): YoutubePlayerViewModel = YoutubePlayerViewModel(state)
    }
}
