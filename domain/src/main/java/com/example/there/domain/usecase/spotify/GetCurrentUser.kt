package com.example.there.domain.usecase.spotify

import com.example.core.model.Resource
import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.spotify.UserEntity
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single

class GetCurrentUser(
    schedulersProvider: UseCaseSchedulersProvider,
    private val remote: ISpotifyRemoteDataStore
) : SingleUseCase<Resource<UserEntity>>(schedulersProvider) {
    override val result: Single<Resource<UserEntity>> get() = remote.currentUser
}
