package com.example.there.domain.usecase.spotify

import com.example.core.model.Resource
import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import com.example.there.domain.usecase.base.ObservableUseCaseWithArgs
import io.reactivex.Observable

class GetAlbumsFromArtist(
        schedulersProvider: UseCaseSchedulersProvider,
        private val remote: ISpotifyRemoteDataStore
) : ObservableUseCaseWithArgs<String, Resource<List<AlbumEntity>>>(schedulersProvider) {
    override fun run(args: String): Observable<Resource<List<AlbumEntity>>> = remote.getAlbumsFromArtist(artistId = args)
}