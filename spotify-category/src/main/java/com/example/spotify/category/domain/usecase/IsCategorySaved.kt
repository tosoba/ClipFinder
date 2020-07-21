package com.example.spotify.category.domain.usecase

import com.example.core.ext.RxSchedulers
import com.example.there.domain.repo.spotify.ISpotifyLocalRepo
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class IsCategorySaved(
    schedulers: RxSchedulers,
    private val repository: ISpotifyLocalRepo
) : SingleUseCaseWithArgs<String, Boolean>(schedulers) {
    override fun run(args: String): Single<Boolean> = repository.isCategorySaved(args)
}