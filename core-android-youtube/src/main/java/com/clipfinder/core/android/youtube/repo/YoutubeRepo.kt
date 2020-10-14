package com.clipfinder.core.android.youtube.repo

import com.clipfinder.core.youtube.ext.single
import com.clipfinder.core.youtube.repo.IYoutubeRepo
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.SearchListResponse
import io.reactivex.Single

class YoutubeRepo(
    private val key: String,
    private val youtube: YouTube
) : IYoutubeRepo {
    override fun search(pageToken: String?): Single<SearchListResponse> = youtube
        .Search()
        .list(listOf("snippet"))
        .setKey(key)
        .run { pageToken?.let(::setPageToken) ?: this }
        .single
}
