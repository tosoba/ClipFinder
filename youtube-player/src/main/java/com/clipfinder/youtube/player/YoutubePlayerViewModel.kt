package com.clipfinder.youtube.player

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.withState
import com.clipfinder.core.android.base.viewmodel.MvRxViewModel
import com.clipfinder.core.android.model.videos.Video
import com.clipfinder.core.android.model.videos.VideoPlaylist

class YoutubePlayerViewModel(initialState: YoutubePlayerState) :
    MvRxViewModel<YoutubePlayerState>(initialState) {
    fun updatePlayerNotificationState(isShowing: Boolean) = setState {
        copy(showingPlaybackNotification = isShowing)
    }

    fun onLoadVideo(video: Video) {
        setState { copy(mode = YoutubePlayerMode.SingleVideo(video)) }
    }

    fun clearLastPlayed() {
        setState { copy(mode = YoutubePlayerMode.Idle) }
    }

    fun updatePlaybackState(inProgress: Boolean) {
        setState { copy(playbackInProgress = inProgress) }
    }

    fun onNextVideoFromPlaylistStarted() {
        setState {
            require(mode is YoutubePlayerMode.Playlist)
            copy(mode = mode.copy(currentVideoIndex = mode.currentVideoIndex + 1))
        }
    }

    fun onLoadVideoPlaylist(videoPlaylist: VideoPlaylist, videos: List<Video>) {
        setState { copy(mode = YoutubePlayerMode.Playlist(videoPlaylist, videos, 0)) }
    }

    fun isAlreadyPlaying(video: Video): Boolean =
        withState(this) { state ->
            state.mode is YoutubePlayerMode.SingleVideo && video == state.mode.video
        }

    fun isAlreadyPlaying(playlist: VideoPlaylist): Boolean =
        withState(this) { state ->
            state.mode is YoutubePlayerMode.Playlist && playlist == state.mode.playlist
        }

    companion object : MvRxViewModelFactory<YoutubePlayerViewModel, YoutubePlayerState> {
        override fun create(viewModelContext: ViewModelContext, state: YoutubePlayerState) =
            YoutubePlayerViewModel(state)
    }
}
