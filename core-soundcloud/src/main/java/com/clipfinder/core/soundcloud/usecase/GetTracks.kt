package com.clipfinder.core.soundcloud.usecase

import com.clipfinder.core.model.UseCaseWithArgs
import com.clipfinder.core.soundcloud.model.ISoundCloudTrack
import com.clipfinder.core.soundcloud.repo.ISoundCloudRepo
import io.reactivex.Single

class GetTracks(private val repo: ISoundCloudRepo) :
    UseCaseWithArgs<List<String>, Single<List<ISoundCloudTrack>>> {
    override fun run(args: List<String>): Single<List<ISoundCloudTrack>> = repo.getTracks(args)
}
