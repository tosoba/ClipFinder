package com.example.youtuberelatedvideos

import com.example.core.android.base.vm.BaseVideosViewModel
import com.example.core.android.mapper.videos.ui
import com.example.core.android.model.videos.Video
import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.usecase.videos.GetChannelsThumbnailUrls
import com.example.there.domain.usecase.videos.SearchRelatedVideos

class RelatedVideosViewModel(
    getChannelsThumbnailUrls: GetChannelsThumbnailUrls,
    private val searchRelatedVideos: SearchRelatedVideos
) : BaseVideosViewModel(getChannelsThumbnailUrls) {

    val viewState = RelatedVideosViewState()

    private var lastSearchVideo: Video? = null

    fun searchRelatedVideosWithToLastId() {
        lastSearchVideo?.let {
            addSearchRelatedVideosDisposable(it, true)
        }
    }

    fun searchRelatedVideos(toVideo: Video) {
        lastSearchVideo = toVideo
        viewState.videos.clear()
        addSearchRelatedVideosDisposable(toVideo, false)
    }

    private fun addSearchRelatedVideosDisposable(video: Video, loadMore: Boolean) {
        if (loadMore) viewState.loadingMoreVideosInProgress.set(true)
        else viewState.initialVideosLoadingInProgress.set(true)

        searchRelatedVideos(SearchRelatedVideos.Args(video.id, loadMore))
            .doFinally {
                if (loadMore) viewState.loadingMoreVideosInProgress.set(false)
                else viewState.initialVideosLoadingInProgress.set(false)
            }
            .takeSuccessOnly()
            .subscribeAndDisposeOnCleared({ videos ->
                val mapped = videos.map(VideoEntity::ui)
                viewState.videos.addAll(mapped)
                getChannelThumbnails(videos, onSuccess = {
                    it.forEach { (index, url) -> mapped.getOrNull(index)?.channelThumbnailUrl?.set(url) }
                })
                viewState.videosLoadingErrorOccurred.set(false)
            }, getOnErrorWith {
                viewState.videosLoadingErrorOccurred.set(true)
            })
    }
}
