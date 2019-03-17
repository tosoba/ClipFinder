package com.example.there.domain.usecase.spotify

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.base.ObservableUseCaseWithInput
import io.reactivex.Observable
import javax.inject.Inject

class GetAlbumsFromArtist @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyRepository
) : ObservableUseCaseWithInput<String, List<AlbumEntity>>(schedulersProvider) {

    override fun createObservable(input: String): Observable<List<AlbumEntity>> = repository.getAlbumsFromArtist(artistId = input)
}