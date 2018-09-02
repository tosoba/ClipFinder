package com.example.there.domain.usecases.spotify

import com.example.there.domain.common.SymmetricObservableTransformer
import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.domain.repos.spotify.ISpotifyRepository
import com.example.there.domain.usecases.UseCaseParams
import com.example.there.domain.usecases.base.ObservableUseCase
import io.reactivex.Observable
import java.lang.IllegalArgumentException

class GetAccessToken(transformer: SymmetricObservableTransformer<AccessTokenEntity>,
                     private val repository: ISpotifyRepository) : ObservableUseCase<AccessTokenEntity>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<AccessTokenEntity> {
        val clientId = data?.get(UseCaseParams.PARAM_CLIENT_ID) as? String
        val clientSecret = data?.get(UseCaseParams.PARAM_CLIENT_SECRET) as? String
        return if (clientId != null && clientSecret != null) {
            repository.getAccessToken(clientId, clientSecret)
        } else {
            Observable.error { IllegalArgumentException("ClientId and ClientSecret must be provided.") }
        }
    }

    fun execute(clientId: String, clientSecret: String): Observable<AccessTokenEntity> {
        val data = HashMap<String, String>().apply {
            put(UseCaseParams.PARAM_CLIENT_ID, clientId)
            put(UseCaseParams.PARAM_CLIENT_SECRET, clientSecret)
        }
        return execute(withData = data)
    }
}