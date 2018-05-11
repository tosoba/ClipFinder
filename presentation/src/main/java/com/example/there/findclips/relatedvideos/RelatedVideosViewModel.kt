package com.example.there.findclips.relatedvideos

import com.example.there.domain.entities.videos.VideoEntity
import com.example.there.domain.usecases.videos.GetChannelsThumbnailUrlsUseCase
import com.example.there.domain.usecases.videos.SearchRelatedVideosUseCase
import com.example.there.findclips.base.BaseViewModel
import com.example.there.findclips.mappers.VideoEntityMapper

class RelatedVideosViewModel(private val searchRelatedVideosUseCase: SearchRelatedVideosUseCase,
                             private val getChannelsThumbnailUrlsUseCase: GetChannelsThumbnailUrlsUseCase): BaseViewModel() {

    val viewState: RelatedVideosViewState = RelatedVideosViewState()

    private var lastSearchVideoId: String? = null
    private var lastSearchRelatedNextPageToken: String? = null

    fun searchRelatedVideosWithToLastId() {
        if (lastSearchVideoId != null && lastSearchRelatedNextPageToken != null) {
            addSearchRelatedVideosDisposable(lastSearchVideoId!!, lastSearchRelatedNextPageToken)
        }
    }

    fun searchRelatedVideos(toVideoId: String) {
        lastSearchVideoId = toVideoId
        addSearchRelatedVideosDisposable(toVideoId, null)
    }

    private fun addSearchRelatedVideosDisposable(videoId: String, pageToken: String?) {
        addDisposable(searchRelatedVideosUseCase.execute(videoId, pageToken)
                .subscribe({
                    val (newNextPageToken, videos) = it
                    lastSearchRelatedNextPageToken = newNextPageToken
                    viewState.videos.addAll(videos.map(VideoEntityMapper::mapFrom))
                    getChannelThumbnails(videos)
                }, this::onError))
    }

    private fun getChannelThumbnails(videos: List<VideoEntity>) {
        addDisposable(getChannelsThumbnailUrlsUseCase.execute(videos)
                .subscribe({
                    it.forEachIndexed { index, url -> viewState.videos.getOrNull(index)?.channelThumbnailUrl?.set(url) }
                }, this::onError))
    }
}