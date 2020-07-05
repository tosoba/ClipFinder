package com.example.spotifydashboard.domain.usecase

import com.example.core.model.Resource
import com.example.spotifydashboard.domain.repo.ISpotifyDashboardRemoteRepo
import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.usecase.base.ObservableUseCase
import io.reactivex.Observable

class GetFeaturedPlaylists(
    schedulersProvider: UseCaseSchedulersProvider,
    private val remote: ISpotifyDashboardRemoteRepo
) : ObservableUseCase<Resource<List<PlaylistEntity>>>(schedulersProvider) {
    override val result: Observable<Resource<List<PlaylistEntity>>> get() = remote.featuredPlaylists
}
