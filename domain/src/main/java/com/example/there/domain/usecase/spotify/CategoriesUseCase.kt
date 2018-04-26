package com.example.there.domain.usecase.spotify

import com.example.there.domain.repos.spotify.SpotifyRepository
import com.example.there.domain.common.Transformer
import com.example.there.domain.common.UseCase
import com.example.there.domain.common.UseCaseParams
import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.domain.entities.spotify.CategoryEntity
import io.reactivex.Observable
import java.lang.IllegalArgumentException

class CategoriesUseCase(transformer: Transformer<List<CategoryEntity>>,
                        private val repository: SpotifyRepository) : UseCase<List<CategoryEntity>>(transformer) {

    override fun createObservable(data: Map<String, Any>?): Observable<List<CategoryEntity>> {
        val accessToken = data?.get(UseCaseParams.PARAM_ACCESS_TOKEN) as? AccessTokenEntity
        return if (accessToken != null) {
            repository.getCategories(accessToken)
        } else {
            Observable.error { IllegalArgumentException("AccessToken must be provided.") }
        }
    }

    fun getCategories(accessToken: AccessTokenEntity): Observable<List<CategoryEntity>> {
        val data = HashMap<String, Any>().apply { put(UseCaseParams.PARAM_ACCESS_TOKEN, accessToken) }
        return observable(withData = data)
    }
}