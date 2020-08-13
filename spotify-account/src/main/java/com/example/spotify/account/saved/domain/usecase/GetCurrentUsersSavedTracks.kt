package com.example.spotify.account.saved.domain.usecase

import com.example.core.ext.RxSchedulers
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.spotify.account.saved.domain.repo.ISpotifyAccountSavedRepo
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetCurrentUsersSavedTracks(
    schedulers: RxSchedulers,
    private val repo: ISpotifyAccountSavedRepo
) : SingleUseCaseWithArgs<Int, Resource<Paged<List<TrackEntity>>>>(schedulers) {
    override fun run(args: Int): Single<Resource<Paged<List<TrackEntity>>>> = repo
        .getCurrentUsersSavedTracks(offset = args)
}
