package com.example.there.domain.usecase.spotify

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import com.example.there.domain.usecase.base.ObservableUseCaseWithArgs
import io.reactivex.Observable
import javax.inject.Inject

class GetAlbumsFromArtist @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val remote: ISpotifyRemoteDataStore
) : ObservableUseCaseWithArgs<String, List<AlbumEntity>>(schedulersProvider) {

    override fun createObservable(args: String): Observable<List<AlbumEntity>> = remote.getAlbumsFromArtist(artistId = args)
}