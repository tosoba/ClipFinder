package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.spotify.auth.ISpotifyAutoAuth
import com.clipfinder.core.spotify.model.ISpotifyCategory
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.example.core.ext.RxSchedulers
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.core.usecase.SingleUseCaseWithArgs
import io.reactivex.Single

class GetCategories(
    schedulers: RxSchedulers,
    private val auth: ISpotifyAutoAuth,
    private val repo: ISpotifyRepo
) : SingleUseCaseWithArgs<Int, Resource<Paged<List<ISpotifyCategory>>>>(schedulers) {
    override fun run(args: Int): Single<Resource<Paged<List<ISpotifyCategory>>>> = auth
        .authorize()
        .andThen(repo.getCategories(offset = args))
}
