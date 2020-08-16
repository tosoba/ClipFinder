package com.example.there.domain.usecase.spotify

import com.example.core.ext.RxSchedulers
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.repo.spotify.ISpotifyLocalRepo
import com.example.there.domain.usecase.base.FlowableUseCase
import io.reactivex.Flowable

class GetFavouriteArtists(
    schedulers: RxSchedulers,
    private val repository: ISpotifyLocalRepo
) : FlowableUseCase<List<ArtistEntity>>(schedulers) {
    override val result: Flowable<List<ArtistEntity>> get() = repository.favouriteArtists
}
