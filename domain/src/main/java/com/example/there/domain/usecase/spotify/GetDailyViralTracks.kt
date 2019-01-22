package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricObservableTransformer
import com.example.there.domain.entity.spotify.TopTrackEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.base.ObservableUseCase
import io.reactivex.Observable

class GetDailyViralTracks(
        transformer: SymmetricObservableTransformer<List<TopTrackEntity>>,
        private val repository: ISpotifyRepository
) : ObservableUseCase<List<TopTrackEntity>>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<List<TopTrackEntity>> = repository.dailyViralTracks
}