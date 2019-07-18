package com.example.there.domain.usecase.spotify

import com.example.core.model.Resource
import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.spotify.TopTrackEntity
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import com.example.there.domain.usecase.base.ObservableUseCase
import io.reactivex.Observable

class GetDailyViralTracks(
        schedulersProvider: UseCaseSchedulersProvider,
        private val remote: ISpotifyRemoteDataStore
) : ObservableUseCase<Resource<List<TopTrackEntity>>>(schedulersProvider) {
    override val result: Observable<Resource<List<TopTrackEntity>>>
        get() = remote.dailyViralTracks
}