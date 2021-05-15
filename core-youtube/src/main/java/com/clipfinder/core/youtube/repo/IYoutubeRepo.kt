package com.clipfinder.core.youtube.repo

import com.clipfinder.core.model.Resource
import com.google.api.services.youtube.model.SearchListResponse
import io.reactivex.Completable
import io.reactivex.Single

interface IYoutubeRepo {
    fun searchVideos(query: String, pageToken: String? = null): Single<Resource<SearchListResponse>>
    fun searchRelatedVideos(
        videoId: String,
        pageToken: String? = null
    ): Single<Resource<SearchListResponse>>
    fun clearExpired(): Completable
}
