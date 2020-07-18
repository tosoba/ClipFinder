package com.example.there.domain.usecase.spotify

import com.example.core.model.Resource
import com.example.core.ext.RxSchedulers
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import com.example.there.domain.usecase.base.ObservableUseCaseWithArgs
import io.reactivex.Observable

class GetAlbumsFromArtist(
    schedulers: RxSchedulers,
    private val remote: ISpotifyRemoteDataStore
) : ObservableUseCaseWithArgs<String, Resource<List<AlbumEntity>>>(schedulers) {
    override fun run(args: String): Observable<Resource<List<AlbumEntity>>> = remote.getAlbumsFromArtist(artistId = args)
}
