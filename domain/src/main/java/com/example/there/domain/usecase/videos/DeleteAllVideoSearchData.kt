package com.example.there.domain.usecase.videos

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.repo.videos.IVideosRepository
import com.example.there.domain.usecase.base.CompletableUseCase
import io.reactivex.Completable
import javax.inject.Inject

class DeleteAllVideoSearchData @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: IVideosRepository
) : CompletableUseCase(schedulersProvider) {

    override val completable: Completable
        get() = repository.deleteAllVideoSearchData()
}