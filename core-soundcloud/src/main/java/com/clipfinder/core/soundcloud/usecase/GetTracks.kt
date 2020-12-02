package com.clipfinder.core.soundcloud.usecase

import com.clipfinder.core.ext.RxSchedulers
import com.clipfinder.core.soundcloud.model.ISoundCloudTrack
import com.clipfinder.core.soundcloud.repo.ISoundCloudRepo
import com.clipfinder.core.usecase.SingleUseCaseWithArgs
import io.reactivex.Single

class GetTracks(
    schedulers: RxSchedulers,
    private val repo: ISoundCloudRepo
) : SingleUseCaseWithArgs<List<String>, List<ISoundCloudTrack>>(schedulers) {
    override fun run(args: List<String>): Single<List<ISoundCloudTrack>> = repo.getTracks(args)
}
