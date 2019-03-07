package com.example.there.findclips.videos.search

import android.view.View
import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.usecase.videos.DeleteVideo
import com.example.there.domain.usecase.videos.GetChannelsThumbnailUrls
import com.example.there.domain.usecase.videos.GetFavouriteVideosFromPlaylist
import com.example.there.domain.usecase.videos.SearchVideos
import com.example.there.findclips.base.vm.BaseVideosViewModel
import com.example.there.findclips.model.entity.VideoPlaylist
import com.example.there.findclips.model.mapper.VideoEntityMapper
import com.example.there.findclips.model.mapper.VideoPlaylistEntityMapper
import com.example.there.findclips.view.list.item.VideoItemView
import javax.inject.Inject

class VideosSearchViewModel @Inject constructor(
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
        addDisposable(searchVideos.execute(SearchVideos.Input(query, loadMore))
                .doFinally { viewState.videosLoadingInProgress.set(false) }
                .subscribe({ videos -> updateVideos(videos) }, ::onError))
    }

    fun getFavouriteVideosFromPlaylist(videoPlaylist: VideoPlaylist) {
        //TODO: check if it isn't broken after usecase changes
        VideoPlaylistEntityMapper.mapBack(videoPlaylist).id?.let { id ->
            addDisposable(getFavouriteVideosFromPlaylist.execute(id)
                    .subscribe({ updateVideos(it, true) }, ::onError))
        }
    }

    private fun updateVideos(videos: List<VideoEntity>, withRemoveOption: Boolean = false) {
        val mapped = videos.map(VideoEntityMapper::mapFrom)

        viewState.videos.addAll(mapped.map { video ->
            VideoItemView(video = video, onRemoveBtnClickListener = if (withRemoveOption) {
                View.OnClickListener {
                    viewState.videos.removeAll { it.video == video }
                    deleteVideo(VideoEntityMapper.mapBack(video))
                }
            } else null)
        })

        getChannelThumbnails(videos, onSuccess = {
            it.forEach { (index, url) -> mapped.getOrNull(index)?.channelThumbnailUrl?.set(url) }
        })
    }

    private fun deleteVideo(video: VideoEntity) = addDisposable(deleteVideo.execute(video).subscribe())
}