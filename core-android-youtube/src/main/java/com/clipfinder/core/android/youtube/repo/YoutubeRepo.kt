package com.clipfinder.core.android.youtube.repo

import com.clipfinder.core.android.youtube.db.dao.SearchDao
import com.clipfinder.core.android.youtube.di.YoutubeQuerySearchStore
import com.clipfinder.core.android.youtube.di.YoutubeRelatedVideosSearchStore
import com.clipfinder.core.android.youtube.di.searchRelatedVideos
import com.clipfinder.core.android.youtube.di.searchVideos
import com.clipfinder.core.ext.resource
import com.clipfinder.core.model.Resource
import com.clipfinder.core.youtube.repo.IYoutubeRepo
import com.google.api.services.youtube.model.SearchListResponse
import io.reactivex.Completable
import io.reactivex.Single

class YoutubeRepo(
    private val querySearchStore: YoutubeQuerySearchStore,
    private val relatedVideosSearchStore: YoutubeRelatedVideosSearchStore,
    private val searchDao: SearchDao
) : IYoutubeRepo {
    override fun searchVideos(
        query: String,
        pageToken: String?
    ): Single<Resource<SearchListResponse>> =
        querySearchStore.searchVideos(query, pageToken).resource

    override fun searchRelatedVideos(
        videoId: String,
        pageToken: String?
    ): Single<Resource<SearchListResponse>> =
        relatedVideosSearchStore.searchRelatedVideos(videoId, pageToken).resource

    override fun clearExpired(): Completable = Completable.fromAction(searchDao::deleteExpired)
}
