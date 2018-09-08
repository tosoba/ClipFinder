package com.example.there.findclips.fragment.search.videos

import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.usecase.videos.GetChannelsThumbnailUrls
import com.example.there.domain.usecase.videos.GetFavouriteVideosFromPlaylist
import com.example.there.domain.usecase.videos.SearchVideos
import com.example.there.findclips.base.vm.BaseVideosViewModel
import com.example.there.findclips.model.entity.VideoPlaylist
import com.example.there.findclips.model.mapper.VideoEntityMapper
import com.example.there.findclips.model.mapper.VideoPlaylistEntityMapper
import javax.inject.Inject

class VideosSearchViewModel @Inject constructor(
        private val searchVideos: SearchVideos,
        getChannelsThumbnailUrls: GetChannelsThumbnailUrls,
        private val getFavouriteVideosFromPlaylist: GetFavouriteVideosFromPlaylist
) : BaseVideosViewModel(getChannelsThumbnailUrls) {

    val viewState: VideosSearchViewState = VideosSearchViewState()

    private var lastQuery: String? = null

    fun searchVideos(query: String) {
        viewState.videosLoadingInProgress.set(true)
        lastQuery = query
        addSearchVideosDisposable(query, false)
    }

    fun searchVideosWithLastQuery() {
        if (lastQuery != null) {
            viewState.videosLoadingInProgress.set(true)
            addSearchVideosDisposable(lastQuery!!, true)
        }
    }

    private fun addSearchVideosDisposable(query: String, loadMore: Boolean) {
        addDisposable(searchVideos.execute(query, loadMore)
                .doFinally { viewState.videosLoadingInProgress.set(false) }
                .subscribe({ videos -> updateVideos(videos) }, ::onError))
    }

    fun getFavouriteVideosFromPlaylist(videoPlaylist: VideoPlaylist) {
        addDisposable(getFavouriteVideosFromPlaylist.execute(VideoPlaylistEntityMapper.mapBack(videoPlaylist))
                .subscribe({ updateVideos(it) }, ::onError))
    }

    private fun updateVideos(videos: List<VideoEntity>) {
        val mapped = videos.map(VideoEntityMapper::mapFrom)
        viewState.videos.addAll(mapped)
        getChannelThumbnails(videos, onSuccess = {
            it.forEach { (index, url) -> mapped.getOrNull(index)?.channelThumbnailUrl?.set(url) }
        })
    }
}