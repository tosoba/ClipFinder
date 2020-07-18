package com.example.there.domain.usecase.spotify

import com.example.core.model.Resource
import com.example.core.ext.RxSchedulers
import com.example.there.domain.entity.spotify.UserEntity
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single

class GetCurrentUser(
    schedulers: RxSchedulers,
    private val remote: ISpotifyRemoteDataStore
) : SingleUseCase<Resource<UserEntity>>(schedulers) {
    override val result: Single<Resource<UserEntity>> get() = remote.currentUser
}
