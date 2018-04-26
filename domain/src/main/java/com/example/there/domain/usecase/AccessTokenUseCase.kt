package com.example.there.domain.usecase

import com.example.there.domain.SpotifyRepository
import com.example.there.domain.common.Transformer
import com.example.there.domain.common.UseCase
import com.example.there.domain.common.UseCaseParams
import com.example.there.domain.entities.AccessTokenEntity
import io.reactivex.Observable
import java.lang.IllegalArgumentException

class AccessTokenUseCase(transformer: Transformer<AccessTokenEntity>,
                         private val repository: SpotifyRepository) : UseCase<AccessTokenEntity>(transformer) {

    override fun createObservable(data: Map<String, Any>?): Observable<AccessTokenEntity> {
        val clientId = data?.get(UseCaseParams.PARAM_CLIENT_ID) as? String
        val clientSecret = data?.get(UseCaseParams.PARAM_CLIENT_SECRET) as? String
        return if (clientId != null && clientSecret != null) {
            repository.getAccessToken(clientId, clientSecret)
        } else {
            Observable.error { IllegalArgumentException("ClientId and ClientSecret must be provided.") }
        }
    }

    fun getAccessToken(clientId: String, clientSecret: String): Observable<AccessTokenEntity> {
        val data = HashMap<String, String>().apply {
            put(UseCaseParams.PARAM_CLIENT_ID, clientId)
            put(UseCaseParams.PARAM_CLIENT_SECRET, clientSecret)
        }
        return observable(withData = data)
    }
}