package com.example.there.domain.usecases.spotify

import com.example.there.domain.common.Transformer
import com.example.there.domain.entities.spotify.CategoryEntity
import com.example.there.domain.repos.spotify.ISpotifyRepository
import com.example.there.domain.usecases.UseCase
import com.example.there.domain.usecases.UseCaseParams
import io.reactivex.Observable

class GetFavouriteCategories(transformer: Transformer<List<CategoryEntity>>,
                             private val repository: ISpotifyRepository) : UseCase<List<CategoryEntity>>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<List<CategoryEntity>> = repository.getFavouriteCategories().toObservable()
}

class InsertCategory(transformer: Transformer<Unit>,
                     private val repository: ISpotifyRepository) : UseCase<Unit>(transformer) {

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