package com.example.spotify.artist.domain.usecase

import com.example.core.ext.RxSchedulers
import com.example.spotify.artist.domain.repo.ISpotifyArtistRepo
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class IsArtistSaved(
    schedulers: RxSchedulers,
    private val repo: ISpotifyArtistRepo
) : SingleUseCaseWithArgs<String, Boolean>(schedulers) {
    override fun run(args: String): Single<Boolean> = repo.isArtistSaved(args)
}
