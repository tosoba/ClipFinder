package com.clipfinder.core.android.youtube.repo

import com.clipfinder.core.android.youtube.db.dao.SearchDao
import com.clipfinder.core.android.youtube.di.YoutubeSearchStore
import com.clipfinder.core.youtube.repo.IYoutubeRepo
import com.dropbox.store.rx2.getSingle
import com.example.core.model.Resource
import com.google.api.services.youtube.model.SearchListResponse
import io.reactivex.Completable
import io.reactivex.Single

class YoutubeRepo(
    private val searchStore: YoutubeSearchStore,
    private val searchDao: SearchDao
) : IYoutubeRepo {

    override fun search(query: String, pageToken: String?): Single<Resource<SearchListResponse>> = searchStore
        .getSingle(query to pageToken)
        .map { Resource.success(it) }
        .onErrorReturn { Resource.error(it) }

    override fun clearExpired(): Completable = Completable.fromAction(searchDao::deleteExpired)
}
