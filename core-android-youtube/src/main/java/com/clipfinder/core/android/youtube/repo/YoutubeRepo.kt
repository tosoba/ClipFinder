package com.clipfinder.core.android.youtube.repo

import com.clipfinder.core.android.youtube.db.dao.SearchDao
import com.clipfinder.core.android.youtube.di.YoutubeSearchStore
import com.clipfinder.core.youtube.repo.IYoutubeRepo
import com.dropbox.store.rx2.getSingle
import com.google.api.services.youtube.model.SearchListResponse
import io.reactivex.Completable
import io.reactivex.Single
import org.threeten.bp.OffsetDateTime

class YoutubeRepo(
    private val searchStore: YoutubeSearchStore,
    private val searchDao: SearchDao
) : IYoutubeRepo {
    override fun search(query: String, pageToken: String?): Single<SearchListResponse> = searchStore
        .getSingle(query to pageToken)

    override fun clearExpired(): Completable = searchDao.deleteExpired()
}
