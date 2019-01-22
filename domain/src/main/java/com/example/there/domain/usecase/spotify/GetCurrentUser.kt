package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricSingleTransformer
import com.example.there.domain.entity.spotify.UserEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single

class GetCurrentUser(
        transformer: SymmetricSingleTransformer<UserEntity>,
        private val repository: ISpotifyRepository
) : SingleUseCase<UserEntity>(transformer) {
    override fun createSingle(data: Map<String, Any?>?): Single<UserEntity> = repository.currentUser
}