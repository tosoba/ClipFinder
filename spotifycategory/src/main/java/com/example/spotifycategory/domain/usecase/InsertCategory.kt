package com.example.spotifycategory.domain.usecase

import com.example.core.ext.RxSchedulers
import com.example.there.domain.entity.spotify.CategoryEntity
import com.example.there.domain.repo.spotify.ISpotifyLocalRepo
import com.example.there.domain.usecase.base.CompletableUseCaseWithArgs
import io.reactivex.Completable

class InsertCategory(
    schedulers: RxSchedulers,
    private val repository: ISpotifyLocalRepo
) : CompletableUseCaseWithArgs<CategoryEntity>(schedulers) {
    override fun run(args: CategoryEntity): Completable = repository.insertCategory(args)
}