package com.example.spotify.artist.domain.usecase

import com.example.core.ext.RxSchedulers
import com.example.core.model.Resource
import com.example.spotify.artist.domain.repo.ISpotifyArtistRepo
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetRelatedArtists(
    schedulers: RxSchedulers,
    private val repo: ISpotifyArtistRepo
) : SingleUseCaseWithArgs<String, Resource<List<ArtistEntity>>>(schedulers) {
    override fun run(
        args: String
    ): Single<Resource<List<ArtistEntity>>> = repo.getRelatedArtists(args)
}
