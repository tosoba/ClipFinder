package com.example.spotify.artist.domain.usecase

import com.example.core.ext.RxSchedulers
import com.example.spotify.artist.domain.repo.ISpotifyArtistRepo
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.usecase.base.CompletableUseCaseWithArgs
import io.reactivex.Completable

class DeleteArtist(
    schedulers: RxSchedulers,
    private val repo: ISpotifyArtistRepo
) : CompletableUseCaseWithArgs<ArtistEntity>(schedulers) {
    override fun run(args: ArtistEntity): Completable = repo.deleteArtist(args)
}
