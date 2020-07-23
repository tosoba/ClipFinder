package com.example.spotify.category.domain.usecase

import com.example.core.ext.RxSchedulers
import com.example.spotify.category.domain.repo.ISpotifyCategoryRepo
import com.example.there.domain.entity.spotify.CategoryEntity
import com.example.there.domain.usecase.base.CompletableUseCaseWithArgs
import io.reactivex.Completable

class InsertCategory(
    schedulers: RxSchedulers,
    private val repo: ISpotifyCategoryRepo
) : CompletableUseCaseWithArgs<CategoryEntity>(schedulers) {
    override fun run(args: CategoryEntity): Completable = repo.insertCategory(args)
}
