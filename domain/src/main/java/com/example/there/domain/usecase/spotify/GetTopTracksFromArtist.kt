package com.example.there.domain.usecase.spotify

import com.example.core.model.Resource
import com.example.core.ext.RxSchedulers
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetTopTracksFromArtist(
    schedulers: RxSchedulers,
    private val remote: ISpotifyRemoteDataStore
) : SingleUseCaseWithArgs<String, Resource<List<TrackEntity>>>(schedulers) {
    override fun run(args: String): Single<Resource<List<TrackEntity>>> = remote.getTopTracksFromArtist(args)
}
