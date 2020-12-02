package com.clipfinder.core.soundcloud.usecase

import com.clipfinder.core.ext.RxSchedulers
import com.clipfinder.core.soundcloud.model.ISoundCloudTrack
import com.clipfinder.core.soundcloud.repo.ISoundCloudRepo
import com.clipfinder.core.usecase.SingleUseCaseWithArgs
import io.reactivex.Single

class GetSimilarTracks(
    schedulers: RxSchedulers,
    private val repo: ISoundCloudRepo
) : SingleUseCaseWithArgs<String, List<ISoundCloudTrack>>(schedulers) {
    override fun run(args: String): Single<List<ISoundCloudTrack>> = repo.getSimilarTracks(args)
}
