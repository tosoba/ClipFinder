package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricSingleTransformer
import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single

class GetAccessToken(
        transformer: SymmetricSingleTransformer<AccessTokenEntity>,
        private val repository: ISpotifyRepository
) : SingleUseCase<AccessTokenEntity>(transformer) {

    override fun createSingle(data: Map<String, Any?>?): Single<AccessTokenEntity> = repository.accessToken
}