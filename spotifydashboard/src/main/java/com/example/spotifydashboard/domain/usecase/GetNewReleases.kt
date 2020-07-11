package com.example.spotifydashboard.domain.usecase

import com.example.core.model.Resource
import com.example.spotifydashboard.domain.repo.ISpotifyDashboardRepo
import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.ListPage
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetNewReleases(
    schedulersProvider: UseCaseSchedulersProvider,
    private val remote: ISpotifyDashboardRepo
) : SingleUseCaseWithArgs<Int, Resource<ListPage<AlbumEntity>>>(schedulersProvider) {
    override fun run(
        args: Int
    ): Single<Resource<ListPage<AlbumEntity>>> = remote.getNewReleases(offset = args)
}
