package com.clipfinder.core.youtube.usecase

import com.clipfinder.core.ext.RxSchedulers
import com.clipfinder.core.usecase.CompletableUseCase
import com.clipfinder.core.youtube.repo.IYoutubeRepo
import io.reactivex.Completable

class ClearExpiredVideosSearchCache(
    private val repo: IYoutubeRepo,
    schedulers: RxSchedulers
) : CompletableUseCase(schedulers) {
    override val result: Completable
        get() = repo.clearExpired()
}
