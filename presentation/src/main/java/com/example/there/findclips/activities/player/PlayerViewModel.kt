package com.example.there.findclips.activities.player

import com.example.there.domain.usecases.videos.*
import com.example.there.findclips.base.vm.BaseVideosViewModel
import com.example.there.findclips.model.entities.Video
import com.example.there.findclips.model.entities.VideoPlaylist
import com.example.there.findclips.model.mappers.VideoEntityMapper
import com.example.there.findclips.model.mappers.VideoPlaylistEntityMapper
import javax.inject.Inject

class PlayerViewModel @Inject constructor(
        private val searchRelatedVideos: SearchRelatedVideos,
        getChannelsThumbnailUrls: GetChannelsThumbnailUrls,
        private val insertVideoPlaylist: InsertVideoPlaylist,
        private val addVideoToPlaylist: AddVideoToPlaylist,
        private val getFavouriteVideoPlaylists: GetFavouriteVideoPlaylists
) : BaseVideosViewModel(getChannelsThumbnailUrls) {

    private var lastSearchVideoId: String? = null

    val viewState = PlayerViewState()

    fun searchRelatedVideosWithToLastId() {
        if (lastSearchVideoId != null) {
            addSearchRelatedVideosDisposable(lastSearchVideoId!!, true)
        }
    }

    fun searchRelatedVideos(toVideoId: String) {
        lastSearchVideoId = toVideoId
        addSearchRelatedVideosDisposable(toVideoId, false)
    }

    private fun addSearchRelatedVideosDisposable(videoId: String, loadMore: Boolean) {
        addDisposable(searchRelatedVideos.execute(videoId, loadMore)
                .subscribe({ videos ->
                    val mapped = videos.map(VideoEntityMapper::mapFrom)
                    viewState.videos.addAll(mapped)
                    getChannelThumbnails(videos, onSuccess = {
                        it.forEach { (index, url) -> mapped.getOrNull(index)?.channelThumbnailUrl?.set(url) }
                    })
                }, this::onError))
    }

    fun addVideoToPlaylist(video: Video, videoPlaylist: VideoPlaylist, onSuccess: () -> Unit) {
        addDisposable(addVideoToPlaylist.execute(playlistEntity = VideoPlaylistEntityMapper.mapBack(videoPlaylist),
                videoEntity = VideoEntityMapper.mapBack(video))
                .subscribe({ onSuccess() }, this::onError))
    }

    fun getFavouriteVideoPlaylists() {
        addDisposable(getFavouriteVideoPlaylists.execute().subscribe({
            viewState.favouriteVideoPlaylists.clear()
            viewState.favouriteVideoPlaylists.addAll(it.map(VideoPlaylistEntityMapper::mapFrom))
        }, this::onError))
    }

    fun addVideoPlaylistWithVideo(playlist: VideoPlaylist, video: Video, onSuccess: () -> Unit) {
        addDisposable(insertVideoPlaylist.execute(VideoPlaylistEntityMapper.mapBack(playlist))
                .subscribe({ playlistId ->
                    addVideoToPlaylist(video, VideoPlaylist(playlistId, playlist.name), onSuccess)
                }, this::onError))
    }
}