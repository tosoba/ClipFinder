package com.example.spotify.dashboard.domain.usecase

import com.example.core.model.Resource
import com.example.spotify.dashboard.domain.repo.ISpotifyDashboardRepo
import com.example.core.ext.RxSchedulers
import com.example.core.model.Paged
import com.example.there.domain.entity.Page
import com.example.there.domain.entity.spotify.TopTrackEntity
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetDailyViralTracks(
    schedulers: RxSchedulers,
    private val remote: ISpotifyDashboardRepo
) : SingleUseCaseWithArgs<Int, Resource<Paged<List<TopTrackEntity>>>>(schedulers) {
    override fun run(
        args: Int
    ): Single<Resource<Paged<List<TopTrackEntity>>>> = remote.getDailyViralTracks(offset = args)
}
