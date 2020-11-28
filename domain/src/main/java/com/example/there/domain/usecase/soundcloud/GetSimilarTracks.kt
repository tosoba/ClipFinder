package com.example.there.domain.usecase.soundcloud

import com.example.core.ext.RxSchedulers
import com.example.core.usecase.SingleUseCaseWithArgs
import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity
import com.example.there.domain.repo.soundcloud.ISoundCloudRemoteDataStore
import io.reactivex.Single

class GetSimilarTracks(
    schedulers: RxSchedulers,
    private val remote: ISoundCloudRemoteDataStore
) : SingleUseCaseWithArgs<String, List<SoundCloudTrackEntity>>(schedulers) {
    override fun run(args: String): Single<List<SoundCloudTrackEntity>> = remote.getSimilarTracks(args)
}
