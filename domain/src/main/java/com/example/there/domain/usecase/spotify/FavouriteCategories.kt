package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricFlowableTransformer
import com.example.there.domain.entity.spotify.CategoryEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.CompletableUseCase
import com.example.there.domain.usecase.base.FlowableUseCase
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.Flowable

class GetFavouriteCategories(
        transformer: SymmetricFlowableTransformer<List<CategoryEntity>>,
        private val repository: ISpotifyRepository
) : FlowableUseCase<List<CategoryEntity>>(transformer) {

    override fun createFlowable(data: Map<String, Any?>?): Flowable<List<CategoryEntity>> = repository.getFavouriteCategories()
}

class InsertCategory(
        transformer: CompletableTransformer,
        private val repository: ISpotifyRepository
) : CompletableUseCase(transformer) {

    override fun createCompletable(data: Map<String, Any?>?): Completable {
        val categoryEntity = data?.get(UseCaseParams.PARAM_CATEGORY) as? CategoryEntity
        return if (categoryEntity != null) {
            repository.insertCategory(categoryEntity)
        } else {
            Completable.error { IllegalArgumentException("CategoryEntity must be provided.") }
        }
    }

    fun execute(categoryEntity: CategoryEntity): Completable {
        val data = HashMap<String, CategoryEntity>().apply {
            put(UseCaseParams.PARAM_CATEGORY, categoryEntity)
        }
        return execute(withData = data)
    }
}