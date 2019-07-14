package com.example.there.domain.usecase.spotify

import com.example.core.model.Resource
import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import com.example.there.domain.usecase.base.ObservableUseCase
import io.reactivex.Observable
import javax.inject.Inject

class GetFeaturedPlaylists @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val remote: ISpotifyRemoteDataStore
) : ObservableUseCase<Resource<List<PlaylistEntity>>>(schedulersProvider) {
    override val result: Observable<Resource<List<PlaylistEntity>>>
        get() = remote.featuredPlaylists
}