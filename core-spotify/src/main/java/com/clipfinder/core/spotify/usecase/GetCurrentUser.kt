package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.model.Resource
import com.clipfinder.core.model.UseCase
import com.clipfinder.core.spotify.model.ISpotifyPrivateUser
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import io.reactivex.Single

class GetCurrentUser(private val repo: ISpotifyRepo) : UseCase<Single<Resource<ISpotifyPrivateUser>>> {
    override val result: Single<Resource<ISpotifyPrivateUser>>
        get() = repo.authorizedUser
}
