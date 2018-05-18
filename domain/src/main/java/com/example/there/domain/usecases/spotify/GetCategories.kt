package com.example.there.domain.usecases.spotify

import com.example.there.domain.repos.spotify.ISpotifyRepository
import com.example.there.domain.common.Transformer
import com.example.there.domain.usecases.UseCase
import com.example.there.domain.usecases.UseCaseParams
import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.domain.entities.spotify.CategoryEntity
import io.reactivex.Observable
import java.lang.IllegalArgumentException

class GetCategories(transformer: Transformer<List<CategoryEntity>>,
                    private val repository: ISpotifyRepository) : UseCase<List<CategoryEntity>>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<List<CategoryEntity>> {
        val accessToken = data?.get(UseCaseParams.PARAM_ACCESS_TOKEN) as? AccessTokenEntity
        return if (accessToken != null) {
            repository.getCategories(accessToken)
        } else {
            Observable.error { IllegalArgumentException("AccessToken must be provided.") }
        }
    }

    fun execute(accessToken: AccessTokenEntity): Observable<List<CategoryEntity>> {
        val data = HashMap<String, Any>().apply { put(UseCaseParams.PARAM_ACCESS_TOKEN, accessToken) }
        return execute(withData = data)
    }
}