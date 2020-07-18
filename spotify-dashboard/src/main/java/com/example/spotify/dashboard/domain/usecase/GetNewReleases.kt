package com.example.spotify.dashboard.domain.usecase

import com.example.core.model.Resource
import com.example.spotify.dashboard.domain.repo.ISpotifyDashboardRepo
import com.example.core.ext.RxSchedulers
import com.example.there.domain.entity.Page
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetNewReleases(
    schedulers: RxSchedulers,
    private val remote: ISpotifyDashboardRepo
) : SingleUseCaseWithArgs<Int, Resource<Page<AlbumEntity>>>(schedulers) {
    override fun run(
        args: Int
    ): Single<Resource<Page<AlbumEntity>>> = remote.getNewReleases(offset = args)
}
