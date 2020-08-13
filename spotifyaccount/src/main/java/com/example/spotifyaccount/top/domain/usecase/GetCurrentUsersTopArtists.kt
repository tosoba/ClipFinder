package com.example.spotifyaccount.top.domain.usecase

import com.example.core.ext.RxSchedulers
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.spotifyaccount.top.domain.repo.ISpotifyAccountTopRepo
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetCurrentUsersTopArtists(
    schedulers: RxSchedulers,
    private val repo: ISpotifyAccountTopRepo
) : SingleUseCaseWithArgs<Int, Resource<Paged<List<ArtistEntity>>>>(schedulers) {
    override fun run(args: Int): Single<Resource<Paged<List<ArtistEntity>>>> = repo
        .getCurrentUsersTopArtists(offset = args)
}
