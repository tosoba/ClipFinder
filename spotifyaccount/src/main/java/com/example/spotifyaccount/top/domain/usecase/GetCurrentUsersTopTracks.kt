package com.example.spotifyaccount.top.domain.usecase

import com.example.core.ext.RxSchedulers
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.spotifyaccount.top.domain.repo.ISpotifyAccountTopRepo
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetCurrentUsersTopTracks(
    schedulers: RxSchedulers,
    private val remote: ISpotifyAccountTopRepo
) : SingleUseCaseWithArgs<Int, Resource<Paged<List<TrackEntity>>>>(schedulers) {
    override fun run(args: Int): Single<Resource<Paged<List<TrackEntity>>>> = remote
        .getCurrentUsersTopTracks(offset = args)
}
