package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.Transformer
import com.example.there.domain.common.UseCase
import com.example.there.domain.common.UseCaseParams
import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.domain.entities.spotify.SearchAllEntity
import com.example.there.domain.repos.spotify.SpotifyRepository
import io.reactivex.Observable
import java.lang.IllegalArgumentException

class SearchAllUseCase(transformer: Transformer<SearchAllEntity>,
                       private val repository: SpotifyRepository) : UseCase<SearchAllEntity>(transformer) {

    override fun createObservable(data: Map<String, Any>?): Observable<SearchAllEntity> {
        val accessToken = data?.get(UseCaseParams.PARAM_ACCESS_TOKEN) as? AccessTokenEntity
        val query = data?.get(UseCaseParams.PARAM_SEARCH_ALL_QUERY) as? String
        return if (accessToken != null && query != null) {
            repository.searchAll(accessToken, query)
        } else {
            Observable.error { IllegalArgumentException("AccessToken and query must be provided.") }
        }
    }

    fun searchAll(accessTokenEntity: AccessTokenEntity, query: String): Observable<SearchAllEntity> {
        val data = HashMap<String, Any>().apply {
            put(UseCaseParams.PARAM_ACCESS_TOKEN, accessTokenEntity)
            put(UseCaseParams.PARAM_SEARCH_ALL_QUERY, query)
        }
        return observable(withData = data)
    }
}