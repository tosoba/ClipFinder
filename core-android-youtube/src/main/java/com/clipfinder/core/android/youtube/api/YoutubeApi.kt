package com.clipfinder.core.android.youtube.api

import com.clipfinder.core.android.youtube.BuildConfig
import com.clipfinder.core.android.youtube.ext.single
import com.clipfinder.core.youtube.api.IYoutubeApi
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.SearchListResponse
import io.reactivex.Single
import timber.log.Timber

class YoutubeApi(private val key: String, private val youtube: YouTube) : IYoutubeApi {
    override fun search(query: String, pageToken: String?): Single<SearchListResponse> = youtube
        .Search()
        .list(listOf("snippet"))
        .setKey(key)
        .setQ(query)
        .run { pageToken?.let(::setPageToken) ?: this }
        .single
        .run {
            if (BuildConfig.DEBUG) {
                doOnSuccess { Timber.d(GsonFactory.getDefaultInstance().toString(it)) }
            } else {
                this
            }
        }
}
