package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.spotify.auth.ISpotifyAuth
import com.clipfinder.core.spotify.model.ISpotifyPrivateUser
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.example.core.model.Resource
import com.example.core.ext.RxSchedulers
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single

class GetCurrentUser(
    schedulers: RxSchedulers,
    private val auth: ISpotifyAuth,
    private val repo: ISpotifyRepo
) : SingleUseCase<Resource<ISpotifyPrivateUser>>(schedulers) {
    override val result: Single<Resource<ISpotifyPrivateUser>>
        get() = auth.requirePrivateAuthorized().andThen(repo.authorizedUser)
}
