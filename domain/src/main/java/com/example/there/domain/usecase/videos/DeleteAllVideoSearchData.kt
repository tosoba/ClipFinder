package com.example.there.domain.usecase.videos

import com.example.there.domain.repo.videos.IVideosRepository
import com.example.there.domain.usecase.base.CompletableUseCase
import io.reactivex.Completable
import io.reactivex.CompletableTransformer

class DeleteAllVideoSearchData(
        transformer: CompletableTransformer,
        private val repository: IVideosRepository
) : CompletableUseCase(transformer) {
    override fun createCompletable(data: Map<String, Any?>?): Completable = repository.deleteAllVideoSearchData()
}