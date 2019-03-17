package com.example.there.domain.usecase.spotify

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.EntityPage
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.base.ObservableUseCaseWithInput
import io.reactivex.Observable
import javax.inject.Inject

class GetTracksFromAlbum @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyRepository
) : ObservableUseCaseWithInput<String, EntityPage<TrackEntity>>(schedulersProvider) {

    override fun createObservable(input: String): Observable<EntityPage<TrackEntity>> = repository.getTracksFromAlbum(input)
}