package com.example.there.domain.usecase.spotify

import com.example.core.model.Resource
import com.example.core.ext.RxSchedulers
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetArtists(
    schedulers: RxSchedulers,
    private val remote: ISpotifyRemoteDataStore
) : SingleUseCaseWithArgs<List<String>, Resource<List<ArtistEntity>>>(schedulers) {
    override fun run(args: List<String>): Single<Resource<List<ArtistEntity>>> = remote
        .getArtists(artistIds = args)
}
