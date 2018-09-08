package com.example.there.findclips.activities.main

import com.example.there.domain.usecases.videos.*
import com.example.there.findclips.base.vm.BaseVideosViewModel
import com.example.there.findclips.model.entities.Video
import com.example.there.findclips.model.entities.VideoPlaylist
import com.example.there.findclips.model.mappers.VideoEntityMapper
import com.example.there.findclips.model.mappers.VideoPlaylistEntityMapper
import javax.inject.Inject

class MainViewModel @Inject constructor(
        private val searchRelatedVideos: SearchRelatedVideos,
        getChannelsThumbnailUrls: GetChannelsThumbnailUrls,
        private val insertVideoPlaylist: InsertVideoPlaylist,
        private val addVideoToPlaylist: AddVideoToPlaylist,
        private val getFavouriteVideoPlaylists: GetFavouriteVideoPlaylists
): BaseVideosViewModel(getChannelsThumbnailUrls) {

    val viewState = MainViewState()

    private var lastSearchVideo: Video? = null

    fun searchRelatedVideosWithToLastId() {
        if (lastSearchVideo != null) {
            addSearchRelatedVideosDisposable(lastSearchVideo!!, true)
        }
    }

    fun searchRelatedVideos(toVideo: Video) {
        lastSearchVideo = toVideo
        addSearchRelatedVideosDisposable(toVideo, false)
    }

    private fun addSearchRelatedVideosDisposable(video: Video, loadMore: Boolean) {
        addDisposable(searchRelatedVideos.execute(video.id, loadMore)
                .subscribe({ videos ->
                    val mapped = videos.map(VideoEntityMapper::mapFrom)
                    viewState.videos.addAll(mapped)
                    getChannelThumbnails(videos, onSuccess = {
                        it.forEach { (index, url) -> mapped.getOrNull(index)?.channelThumbnailUrl?.set(url) }
                    })
                }, ::onError))
    }

    fun addVideoToPlaylist(video: Video, videoPlaylist: VideoPlaylist, onSuccess: () -> Unit) {
        addDisposable(addVideoToPlaylist.execute(playlistEntity = VideoPlaylistEntityMapper.mapBack(videoPlaylist),
                videoEntity = VideoEntityMapper.mapBack(video))
                .subscribe({ onSuccess() }, ::onError))
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