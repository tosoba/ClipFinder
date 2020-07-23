package com.example.spotifyartist.domain.usecase

import com.example.core.ext.RxSchedulers
import com.example.spotifyartist.domain.repo.ISpotifyArtistRepo
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class IsArtistSaved(
    schedulers: RxSchedulers,
    private val repo: ISpotifyArtistRepo
) : SingleUseCaseWithArgs<String, Boolean>(schedulers) {
    override fun run(args: String): Single<Boolean> = repo.isArtistSaved(args)
}
