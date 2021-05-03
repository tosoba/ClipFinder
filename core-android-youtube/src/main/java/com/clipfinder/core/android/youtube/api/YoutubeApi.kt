package com.clipfinder.core.android.youtube.api

import com.clipfinder.core.android.youtube.BuildConfig
import com.clipfinder.core.android.youtube.ext.single
import com.clipfinder.core.youtube.api.IYoutubeApi
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.SearchListResponse
import io.reactivex.Single
import timber.log.Timber

class YoutubeApi(private val youtube: YouTube) : IYoutubeApi {
    override fun search(query: String, pageToken: String?): Single<SearchListResponse> =
        youtube.Search()
            .list(listOf("snippet"))
            .setType(listOf("video"))
            .setKey(BuildConfig.YOUTUBE_API_KEY)
            .setQ(query)
            .run { pageToken?.let(::setPageToken) ?: this }
            .single
            .logOnSuccess

    override fun searchRelatedVideos(
        videoId: String,
        pageToken: String?
    ): Single<SearchListResponse> =
        youtube.Search()
            .list(listOf("snippet"))
            .setType(listOf("video"))
            .setKey(BuildConfig.YOUTUBE_API_KEY)
            .setRelatedToVideoId(videoId)
            .run { pageToken?.let(::setPageToken) ?: this }
            .single
            .logOnSuccess

    private val <T> Single<T>.logOnSuccess: Single<T>
        get() = run {
            if (BuildConfig.DEBUG)
                doOnSuccess { Timber.d(GsonFactory.getDefaultInstance().toString(it)) }
            else {
                this
            }
        }
}
