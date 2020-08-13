package com.example.spotifyaccount.saved.domain.usecase

import com.example.core.ext.RxSchedulers
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.spotifyaccount.saved.domain.repo.ISpotifyAccountSavedRepo
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetCurrentUsersSavedAlbums(
    schedulers: RxSchedulers,
    private val repo: ISpotifyAccountSavedRepo
) : SingleUseCaseWithArgs<Int, Resource<Paged<List<AlbumEntity>>>>(schedulers) {
    override fun run(args: Int): Single<Resource<Paged<List<AlbumEntity>>>> = repo
        .getCurrentUsersSavedAlbums(offset = args)
}
