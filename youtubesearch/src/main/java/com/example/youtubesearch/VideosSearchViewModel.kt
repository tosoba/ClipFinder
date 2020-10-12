package com.example.youtubesearch

import com.example.core.android.base.vm.BaseVideosViewModel
import com.example.core.android.mapper.videos.ui
import com.example.core.android.view.recyclerview.item.VideoItemView
import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.usecase.videos.GetChannelsThumbnailUrls
import com.example.there.domain.usecase.videos.SearchVideos

class VideosSearchViewModel(
    private val searchVideos: SearchVideos,
    getChannelsThumbnailUrls: GetChannelsThumbnailUrls
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
        searchVideos(SearchVideos.Args(query, loadMore))
            .doFinally { viewState.videosLoadingInProgress.set(false) }
            .takeSuccessOnly()
            .subscribeAndDisposeOnCleared({ videos ->
                updateVideos(videos)
                viewState.videosLoadingErrorOccurred.set(false)
            }, getOnErrorWith {
                viewState.videosLoadingErrorOccurred.set(true)
            }
            )
    }

    private fun updateVideos(videos: List<VideoEntity>, withRemoveOption: Boolean = false) {
        val mapped = videos.map(VideoEntity::ui)

        viewState.videos.addAll(mapped.map { video ->
            VideoItemView(
                video = video,
                onRemoveBtnClickListener = null
            )
        })

        getChannelThumbnails(videos, onSuccess = {
            it.forEach { (index, url) -> mapped.getOrNull(index)?.channelThumbnailUrl?.set(url) }
        })
    }
}
