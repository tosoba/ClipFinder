package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricSingleTransformer
import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single
import java.lang.IllegalArgumentException

class GetAccessToken(
        transformer: SymmetricSingleTransformer<AccessTokenEntity>,
        private val repository: ISpotifyRepository
) : SingleUseCase<AccessTokenEntity>(transformer) {

    override fun createSingle(data: Map<String, Any?>?): Single<AccessTokenEntity> {
        val clientId = data?.get(UseCaseParams.PARAM_CLIENT_ID) as? String
        val clientSecret = data?.get(UseCaseParams.PARAM_CLIENT_SECRET) as? String
        return if (clientId != null && clientSecret != null) {
            repository.getAccessToken(clientId, clientSecret)
        } else {
            Single.error { IllegalArgumentException("ClientId and ClientSecret must be provided.") }
        }
    }

    fun execute(clientId: String, clientSecret: String): Single<AccessTokenEntity> {
        val data = HashMap<String, String>().apply {
            put(UseCaseParams.PARAM_CLIENT_ID, clientId)
            put(UseCaseParams.PARAM_CLIENT_SECRET, clientSecret)
        }
        return execute(withData = data)
    }
}