package com.example.youtubesearch

import android.view.View
import com.example.core.android.base.vm.BaseVideosViewModel
import com.example.core.android.mapper.videos.domain
import com.example.core.android.mapper.videos.ui
import com.example.core.android.model.videos.VideoPlaylist
import com.example.core.android.view.recyclerview.item.VideoItemView
import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.usecase.videos.DeleteVideo
import com.example.there.domain.usecase.videos.GetChannelsThumbnailUrls
import com.example.there.domain.usecase.videos.GetFavouriteVideosFromPlaylist
import com.example.there.domain.usecase.videos.SearchVideos

class VideosSearchViewModel(
    private val searchVideos: SearchVideos,
    getChannelsThumbnailUrls: GetChannelsThumbnailUrls,
    private val getFavouriteVideosFromPlaylist: GetFavouriteVideosFromPlaylist,
    private val deleteVideo: DeleteVideo
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
                viewState.videosLoadingErrorOccurred.set(true) }
            )
    }

    fun getFavouriteVideosFromPlaylist(videoPlaylist: VideoPlaylist) {
        //TODO: check if it isn't broken after usecase changes
        videoPlaylist.domain.id?.let { id ->
            getFavouriteVideosFromPlaylist(id)
                .subscribeAndDisposeOnCleared({ updateVideos(it, true) }, ::onError)
        }
    }

    private fun updateVideos(videos: List<VideoEntity>, withRemoveOption: Boolean = false) {
        val mapped = videos.map(VideoEntity::ui)

        viewState.videos.addAll(mapped.map { video ->
            VideoItemView(
                video = video,
                onRemoveBtnClickListener = if (withRemoveOption) View.OnClickListener {
                    viewState.videos.removeAll { it.video == video }
                    deleteVideo(video.domain)
                } else null
            )
        })

        getChannelThumbnails(videos, onSuccess = {
            it.forEach { (index, url) -> mapped.getOrNull(index)?.channelThumbnailUrl?.set(url) }
        })
    }

    private fun deleteVideo(video: VideoEntity) = deleteVideo.invoke(video)
        .subscribeAndDisposeOnCleared()
}
