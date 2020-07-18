package com.example.spotifydashboard.domain.usecase

import com.example.core.model.Resource
import com.example.spotifydashboard.domain.repo.ISpotifyDashboardRepo
import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.Page
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetNewReleases(
    schedulersProvider: UseCaseSchedulersProvider,
    private val remote: ISpotifyDashboardRepo
) : SingleUseCaseWithArgs<Int, Resource<Page<AlbumEntity>>>(schedulersProvider) {
    override fun run(
        args: Int
    ): Single<Resource<Page<AlbumEntity>>> = remote.getNewReleases(offset = args)
}
