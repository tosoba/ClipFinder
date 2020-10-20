package com.clipfinder.core.youtube.repo

import com.google.api.services.youtube.model.SearchListResponse
import io.reactivex.Completable
import io.reactivex.Single

interface IYoutubeRepo {
    fun search(query: String, pageToken: String? = null): Single<SearchListResponse>
    fun clearExpired(): Completable
}
