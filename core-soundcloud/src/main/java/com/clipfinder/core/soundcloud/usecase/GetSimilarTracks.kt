package com.clipfinder.core.soundcloud.usecase

import com.clipfinder.core.soundcloud.model.ISoundCloudTrack
import com.clipfinder.core.soundcloud.repo.ISoundCloudRepo
import com.clipfinder.core.model.UseCaseWithArgs
import io.reactivex.Single

class GetSimilarTracks(
    private val repo: ISoundCloudRepo
) : UseCaseWithArgs<String, Single<List<ISoundCloudTrack>>> {
    override fun run(args: String): Single<List<ISoundCloudTrack>> = repo.getSimilarTracks(args)
}
