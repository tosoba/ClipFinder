package com.example.spotify.artist.domain.usecase

import com.example.core.ext.RxSchedulers
import com.example.core.model.Resource
import com.example.spotify.artist.domain.repo.ISpotifyArtistRepo
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetTopTracksFromArtist(
    schedulers: RxSchedulers,
    private val repo: ISpotifyArtistRepo
) : SingleUseCaseWithArgs<String, Resource<List<TrackEntity>>>(schedulers) {
    override fun run(
        args: String
    ): Single<Resource<List<TrackEntity>>> = repo.getTopTracksFromArtist(args)
}
