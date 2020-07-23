package com.example.spotify.category.domain.usecase

import com.example.core.ext.RxSchedulers
import com.example.spotify.category.domain.repo.ISpotifyCategoryRepo
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class IsCategorySaved(
    schedulers: RxSchedulers,
    private val repo: ISpotifyCategoryRepo
) : SingleUseCaseWithArgs<String, Boolean>(schedulers) {
    override fun run(args: String): Single<Boolean> = repo.isCategorySaved(args)
}
