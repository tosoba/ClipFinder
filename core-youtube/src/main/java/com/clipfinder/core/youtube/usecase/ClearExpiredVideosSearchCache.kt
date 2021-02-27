package com.clipfinder.core.youtube.usecase

import com.clipfinder.core.model.UseCase
import com.clipfinder.core.youtube.repo.IYoutubeRepo
import io.reactivex.Completable

class ClearExpiredVideosSearchCache(private val repo: IYoutubeRepo) : UseCase<Completable> {
    override val result: Completable
        get() = repo.clearExpired()
}
