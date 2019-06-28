package com.example.there.domain.usecase.spotify

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import com.example.there.domain.usecase.base.ObservableUseCaseWithArgs
import io.reactivex.Observable
import javax.inject.Inject

class GetSimilarTracks @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val remote: ISpotifyRemoteDataStore
) : ObservableUseCaseWithArgs<String, List<TrackEntity>>(schedulersProvider) {

    override fun createObservable(args: String): Observable<List<TrackEntity>> = remote.getSimilarTracks(args)
}