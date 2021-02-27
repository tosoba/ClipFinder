package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.model.Resource
import com.clipfinder.core.spotify.auth.ISpotifyAutoAuth
import com.clipfinder.core.spotify.model.ISpotifyPrivateUser
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.clipfinder.core.model.UseCase
import io.reactivex.Single

class GetCurrentUser(
    private val auth: ISpotifyAutoAuth,
    private val repo: ISpotifyRepo
) : UseCase<Single<Resource<ISpotifyPrivateUser>>> {
    override val result: Single<Resource<ISpotifyPrivateUser>>
        get() = auth.requirePrivateAuthorized().andThen(repo.authorizedUser)
}
