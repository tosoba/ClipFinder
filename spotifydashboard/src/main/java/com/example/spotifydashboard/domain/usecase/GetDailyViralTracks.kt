package com.example.spotifydashboard.domain.usecase

import com.example.core.model.Resource
import com.example.spotifydashboard.domain.repo.ISpotifyDashboardRepo
import com.example.core.ext.RxSchedulers
import com.example.there.domain.entity.Page
import com.example.there.domain.entity.spotify.TopTrackEntity
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetDailyViralTracks(
    schedulers: RxSchedulers,
    private val remote: ISpotifyDashboardRepo
) : SingleUseCaseWithArgs<Int, Resource<Page<TopTrackEntity>>>(schedulers) {
    override fun run(
        args: Int
    ): Single<Resource<Page<TopTrackEntity>>> = remote.getDailyViralTracks(offset = args)
}
