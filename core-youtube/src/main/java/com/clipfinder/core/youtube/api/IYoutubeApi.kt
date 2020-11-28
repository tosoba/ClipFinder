package com.clipfinder.core.youtube.api

import com.google.api.services.youtube.model.SearchListResponse
import io.reactivex.Single

interface IYoutubeApi {
    fun search(query: String, pageToken: String? = null): Single<SearchListResponse>
    fun searchRelatedVideos(videoId: String, pageToken: String? = null): Single<SearchListResponse>
}
