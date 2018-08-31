package com.example.there.findclips.fragments.search.videos

import com.example.there.domain.entities.videos.VideoEntity
import com.example.there.domain.usecases.videos.GetChannelsThumbnailUrls
import com.example.there.domain.usecases.videos.GetFavouriteVideosFromPlaylist
import com.example.there.domain.usecases.videos.SearchVideos
import com.example.there.findclips.base.vm.BaseVideosViewModel
import com.example.there.findclips.model.entities.VideoPlaylist
import com.example.there.findclips.model.mappers.VideoEntityMapper
import com.example.there.findclips.model.mappers.VideoPlaylistEntityMapper
import javax.inject.Inject

class VideosSearchViewModel @Inject constructor(
        private val searchVideos: SearchVideos,
        getChannelsThumbnailUrls: GetChannelsThumbnailUrls,
        private val getFavouriteVideosFromPlaylist: GetFavouriteVideosFromPlaylist
) : BaseVideosViewModel(getChannelsThumbnailUrls) {

    val viewState: VideosSearchViewState = VideosSearchViewState()

    private var lastSearchVideosNextPageToken: String? = null
    private var lastQuery: String? = null

    fun searchVideos(query: String) {
        viewState.videosLoadingInProgress.set(true)
        lastQuery = query
        addSearchVideosDisposable(query, null)
    }

    fun searchVideosWithLastQuery() {
        if (lastQuery != null && lastSearchVideosNextPageToken != null) {
            viewState.videosLoadingInProgress.set(true)
            addSearchVideosDisposable(lastQuery!!, lastSearchVideosNextPageToken)
        }
    }

    private fun addSearchVideosDisposable(query: String, pageToken: String?) {
        addDisposable(searchVideos.execute(query, pageToken)
                .doFinally { viewState.videosLoadingInProgress.set(false) }
                .subscribe({
                    val (newNextPageToken, videos) = it
                    lastSearchVideosNextPageToken = newNextPageToken
                    updateVideos(videos)
                }, this::onError))
    }

    fun getFavouriteVideosFromPlaylist(videoPlaylist: VideoPlaylist) {
        addDisposable(getFavouriteVideosFromPlaylist.execute(VideoPlaylistEntityMapper.mapBack(videoPlaylist))
                .subscribe({ updateVideos(it) }, this::onError))
    }

    private fun updateVideos(videos: List<VideoEntity>) {
        viewState.videos.addAll(videos.map(VideoEntityMapper::mapFrom))
        getChannelThumbnails(videos, onSuccess = {
            it.forEachIndexed { index, url -> viewState.videos.getOrNull(index)?.channelThumbnailUrl?.set(url) }
        })
    }
}