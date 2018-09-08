package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricObservableTransformer
import com.example.there.domain.entity.spotify.CategoryEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.ObservableUseCase
import io.reactivex.Observable

class GetFavouriteCategories(transformer: SymmetricObservableTransformer<List<CategoryEntity>>,
                             private val repository: ISpotifyRepository) : ObservableUseCase<List<CategoryEntity>>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<List<CategoryEntity>> = repository.getFavouriteCategories().toObservable()
}

class InsertCategory(transformer: SymmetricObservableTransformer<Unit>,
                     private val repository: ISpotifyRepository) : ObservableUseCase<Unit>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<Unit> {
        val categoryEntity = data?.get(UseCaseParams.PARAM_CATEGORY) as? CategoryEntity
        return if (categoryEntity != null) {
            repository.insertCategory(categoryEntity).toObservable()
        } else {
            Observable.error { IllegalArgumentException("CategoryEntity must be provided.") }
        }
    }

    fun execute(categoryEntity: CategoryEntity): Observable<Unit> {
        val data = HashMap<String, CategoryEntity>().apply {
            put(UseCaseParams.PARAM_CATEGORY, categoryEntity)
        }
        return execute(withData = data)
    }
}