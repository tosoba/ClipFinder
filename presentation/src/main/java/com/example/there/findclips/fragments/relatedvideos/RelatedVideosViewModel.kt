package com.example.there.findclips.fragments.relatedvideos

import com.example.there.domain.usecases.videos.GetChannelsThumbnailUrls
import com.example.there.domain.usecases.videos.SearchRelatedVideos
import com.example.there.findclips.base.BaseVideosViewModel
import com.example.there.findclips.model.mappers.VideoEntityMapper

class RelatedVideosViewModel(private val searchRelatedVideos: SearchRelatedVideos,
                             getChannelsThumbnailUrls: GetChannelsThumbnailUrls) : BaseVideosViewModel(getChannelsThumbnailUrls) {

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
        addDisposable(searchRelatedVideos.execute(videoId, pageToken)
                .subscribe({
                    val (newNextPageToken, videos) = it
                    lastSearchRelatedNextPageToken = newNextPageToken
                    viewState.videos.addAll(videos.map(VideoEntityMapper::mapFrom))
                    getChannelThumbnails(videos, onSuccess = {
                        it.forEachIndexed { index, url -> viewState.videos.getOrNull(index)?.channelThumbnailUrl?.set(url) }
                    })
                }, this::onError))
    }
}