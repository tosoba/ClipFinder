package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.spotify.auth.ISpotifyAuth
import com.clipfinder.core.spotify.model.ISpotifyCategory
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.example.core.ext.RxSchedulers
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetCategories(
    schedulers: RxSchedulers,
    private val auth: ISpotifyAuth,
    private val repo: ISpotifyRepo
) : SingleUseCaseWithArgs<Int, Resource<Paged<List<ISpotifyCategory>>>>(schedulers) {
    override fun run(args: Int): Single<Resource<Paged<List<ISpotifyCategory>>>> = auth
        .authorize()
        .andThen(repo.getCategories(offset = args))
}
