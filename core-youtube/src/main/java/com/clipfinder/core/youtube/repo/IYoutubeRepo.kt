package com.clipfinder.core.youtube.repo

import com.example.core.model.Resource
import com.google.api.services.youtube.model.SearchListResponse
import io.reactivex.Completable
import io.reactivex.Single

interface IYoutubeRepo {
    fun search(query: String, pageToken: String? = null): Single<Resource<SearchListResponse>>
    fun searchRelated(videoId: String, pageToken: String? = null): Single<Resource<SearchListResponse>>
    fun clearExpired(): Completable
}
