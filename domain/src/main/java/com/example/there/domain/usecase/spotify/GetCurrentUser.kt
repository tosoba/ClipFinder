package com.example.there.domain.usecase.spotify

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.spotify.UserEntity
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class GetCurrentUser @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val remote: ISpotifyRemoteDataStore
) : SingleUseCase<UserEntity>(schedulersProvider) {

    override val single: Single<UserEntity>
        get() = remote.currentUser
}