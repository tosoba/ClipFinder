package com.example.there.domain.usecase.spotify

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.EntityPage
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import com.example.there.domain.usecase.base.ObservableUseCaseWithArgs
import io.reactivex.Observable
import javax.inject.Inject

class GetTracksFromAlbum @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val remote: ISpotifyRemoteDataStore
) : ObservableUseCaseWithArgs<String, EntityPage<TrackEntity>>(schedulersProvider) {

    override fun createObservable(args: String): Observable<EntityPage<TrackEntity>> = remote.getTracksFromAlbum(args)
}