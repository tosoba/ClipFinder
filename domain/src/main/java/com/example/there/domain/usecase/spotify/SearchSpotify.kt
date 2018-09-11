package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricObservableTransformer
import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.domain.entity.spotify.SearchAllEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.ObservableUseCase
import io.reactivex.Observable
import java.lang.IllegalArgumentException

class SearchSpotify(transformer: SymmetricObservableTransformer<SearchAllEntity>,
                    private val repository: ISpotifyRepository) : ObservableUseCase<SearchAllEntity>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<SearchAllEntity> {
        val accessToken = data?.get(UseCaseParams.PARAM_ACCESS_TOKEN) as? AccessTokenEntity
        val query = data?.get(UseCaseParams.PARAM_SEARCH_ALL_QUERY) as? String
        return if (accessToken != null && query != null) {
            repository.searchAll(accessToken, query)
        } else {
            Observable.error { IllegalArgumentException("AccessToken and query must be provided.") }
        }
    }

    fun execute(accessTokenEntity: AccessTokenEntity, query: String): Observable<SearchAllEntity> {
        val data = HashMap<String, Any>().apply {
            put(UseCaseParams.PARAM_ACCESS_TOKEN, accessTokenEntity)
            put(UseCaseParams.PARAM_SEARCH_ALL_QUERY, query)
        }
        return execute(withData = data)
    }
}