package com.example.spotifydashboard.domain.usecase

import com.example.core.model.Resource
import com.example.spotifydashboard.domain.repo.ISpotifyDashboardRemoteRepo
import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.ListPage
import com.example.there.domain.entity.spotify.CategoryEntity
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetCategories(
    schedulersProvider: UseCaseSchedulersProvider,
    private val remote: ISpotifyDashboardRemoteRepo
) : SingleUseCaseWithArgs<Int, Resource<ListPage<CategoryEntity>>>(schedulersProvider) {
    override fun run(args: Int): Single<Resource<ListPage<CategoryEntity>>> = remote.getCategories(offset = args)
}
