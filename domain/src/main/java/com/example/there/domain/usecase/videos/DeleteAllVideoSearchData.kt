package com.example.there.domain.usecase.videos

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.repo.videos.IVideosDbDataStore
import com.example.there.domain.usecase.base.CompletableUseCase
import io.reactivex.Completable
import javax.inject.Inject

class DeleteAllVideoSearchData @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val local: IVideosDbDataStore
) : CompletableUseCase(schedulersProvider) {

    override val completable: Completable
        get() = local.deleteAllVideoSearchData()
}