package com.example.there.domain.usecase.spotify

import com.example.core.model.Resource
import com.example.core.ext.RxSchedulers
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetAlbum(
    schedulers: RxSchedulers,
    private val remote: ISpotifyRemoteDataStore
) : SingleUseCaseWithArgs<String, Resource<AlbumEntity>>(schedulers) {
    override fun run(args: String): Single<Resource<AlbumEntity>> = remote.getAlbum(albumId = args)
}
