package com.example.there.domain.usecase.videos

import com.example.core.ext.RxSchedulers
import com.example.there.domain.repo.videos.IVideosDbDataStore
import com.example.there.domain.usecase.base.CompletableUseCase
import io.reactivex.Completable

class DeleteAllVideoSearchData(
    schedulers: RxSchedulers,
    private val local: IVideosDbDataStore
) : CompletableUseCase(schedulers) {
    override val result: Completable get() = local.deleteAllVideoSearchData()
}
