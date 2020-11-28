package com.clipfinder.core.youtube.usecase

import com.clipfinder.core.youtube.repo.IYoutubeRepo
import com.example.core.ext.RxSchedulers
import com.example.core.usecase.CompletableUseCase
import io.reactivex.Completable

class ClearExpiredVideosSearchCache(
    private val repo: IYoutubeRepo,
    schedulers: RxSchedulers
) : CompletableUseCase(schedulers) {
    override val result: Completable
        get() = repo.clearExpired()
}
