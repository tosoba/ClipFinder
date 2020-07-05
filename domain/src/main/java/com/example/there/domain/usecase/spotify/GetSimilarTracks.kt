package com.example.there.domain.usecase.spotify

import com.example.core.model.Resource
import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import com.example.there.domain.usecase.base.ObservableUseCaseWithArgs
import io.reactivex.Observable

class GetSimilarTracks(
    schedulersProvider: UseCaseSchedulersProvider,
    private val remote: ISpotifyRemoteDataStore
) : ObservableUseCaseWithArgs<String, Resource<List<TrackEntity>>>(schedulersProvider) {
    override fun run(args: String): Observable<Resource<List<TrackEntity>>> = remote.getSimilarTracks(args)
}
