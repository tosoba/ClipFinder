package com.clipfinder.core.youtube.repo

import com.google.api.services.youtube.model.SearchListResponse
import io.reactivex.Single

interface IYoutubeRepo {
    fun search(pageToken: String? = null): Single<SearchListResponse>
}
