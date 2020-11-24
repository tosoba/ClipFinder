package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.spotify.auth.ISpotifyAutoAuth
import com.clipfinder.core.spotify.model.ISpotifyPrivateUser
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.example.core.ext.RxSchedulers
import com.example.core.model.Resource
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single

class GetCurrentUser(
    schedulers: RxSchedulers,
    private val auth: ISpotifyAutoAuth,
    private val repo: ISpotifyRepo
) : SingleUseCase<Resource<ISpotifyPrivateUser>>(schedulers) {
    override val result: Single<Resource<ISpotifyPrivateUser>>
        get() = auth.requirePrivateAuthorized().andThen(repo.authorizedUser)
}
