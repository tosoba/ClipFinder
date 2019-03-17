package com.example.there.domain.usecase.spotify

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.spotify.UserEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class GetCurrentUser @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyRepository
) : SingleUseCase<UserEntity>(schedulersProvider) {

    override val single: Single<UserEntity>
        get() = repository.currentUser
}