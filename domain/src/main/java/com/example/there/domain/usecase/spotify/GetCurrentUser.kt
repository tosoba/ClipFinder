package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricSingleTransformer
import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.domain.entity.spotify.UserEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single

class GetCurrentUser(
        transformer: SymmetricSingleTransformer<UserEntity>,
        private val repository: ISpotifyRepository
) : SingleUseCase<UserEntity>(transformer) {
    override fun createSingle(data: Map<String, Any?>?): Single<UserEntity> {
        val accessTokenEntity = data?.get(UseCaseParams.PARAM_ACCESS_TOKEN) as? AccessTokenEntity
        return if (accessTokenEntity != null) {
            repository.getCurrentUser(accessTokenEntity)
        } else {
            Single.error { IllegalArgumentException("Access token must be provided.") }
        }
    }

    fun execute(accessTokenEntity: AccessTokenEntity): Single<UserEntity> {
        val data = HashMap<String, Any>().apply {
            put(UseCaseParams.PARAM_ACCESS_TOKEN, accessTokenEntity)
        }
        return execute(withData = data)
    }
}