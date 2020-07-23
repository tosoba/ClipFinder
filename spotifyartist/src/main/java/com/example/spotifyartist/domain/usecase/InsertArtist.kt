package com.example.spotifyartist.domain.usecase

import com.example.core.ext.RxSchedulers
import com.example.spotifyartist.domain.repo.ISpotifyArtistRepo
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.usecase.base.CompletableUseCaseWithArgs
import io.reactivex.Completable

class InsertArtist(
    schedulers: RxSchedulers,
    private val repo: ISpotifyArtistRepo
) : CompletableUseCaseWithArgs<ArtistEntity>(schedulers) {
    override fun run(args: ArtistEntity): Completable = repo.insertArtist(args)
}
