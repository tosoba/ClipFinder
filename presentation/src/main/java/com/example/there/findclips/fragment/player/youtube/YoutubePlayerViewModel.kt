package com.example.there.findclips.fragment.player.youtube

import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import com.example.there.domain.usecase.videos.AddVideoToPlaylist
import com.example.there.domain.usecase.videos.GetFavouriteVideoPlaylists
import com.example.there.domain.usecase.videos.InsertVideoPlaylist
import com.example.there.findclips.base.vm.BaseViewModel
import com.example.there.findclips.model.entity.Video
import com.example.there.findclips.model.entity.VideoPlaylist
import com.example.there.findclips.model.mapper.VideoEntityMapper
import com.example.there.findclips.model.mapper.VideoPlaylistEntityMapper
import javax.inject.Inject

class YoutubePlayerViewState(
        val favouriteVideoPlaylists: ObservableList<VideoPlaylist> = ObservableArrayList<VideoPlaylist>()
)

class YoutubePlayerViewModel @Inject constructor(
        private val insertVideoPlaylist: InsertVideoPlaylist,
        private val addVideoToPlaylist: AddVideoToPlaylist,
        private val getFavouriteVideoPlaylists: GetFavouriteVideoPlaylists
) : BaseViewModel() {

    val viewState = YoutubePlayerViewState()

    fun addVideoToPlaylist(video: Video, videoPlaylist: VideoPlaylist, onSuccess: () -> Unit) {
        addDisposable(addVideoToPlaylist.execute(AddVideoToPlaylist.Input(
                playlistEntity = VideoPlaylistEntityMapper.mapBack(videoPlaylist),
                videoEntity = VideoEntityMapper.mapBack(video))
        ).subscribe(onSuccess, ::onError))
    }

    fun getFavouriteVideoPlaylists() {
        addDisposable(getFavouriteVideoPlaylists.execute().subscribe({
            viewState.favouriteVideoPlaylists.clear()
            viewState.favouriteVideoPlaylists.addAll(it.map(VideoPlaylistEntityMapper::mapFrom))
        }, ::onError))
    }

    fun addVideoPlaylistWithVideo(playlist: VideoPlaylist, video: Video, onSuccess: () -> Unit) {
        addDisposable(insertVideoPlaylist.execute(VideoPlaylistEntityMapper.mapBack(playlist))
                .subscribe({ playlistId ->
                    addVideoToPlaylist(video, VideoPlaylist(playlistId, playlist.name), onSuccess)
                }, ::onError))
    }
}