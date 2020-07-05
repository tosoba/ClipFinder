package com.example.there.domain.usecase.soundcloud

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity
import com.example.there.domain.repo.soundcloud.ISoundCloudRemoteDataStore
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetSimilarTracks(
    schedulersProvider: UseCaseSchedulersProvider,
    private val remote: ISoundCloudRemoteDataStore
) : SingleUseCaseWithArgs<String, List<SoundCloudTrackEntity>>(schedulersProvider) {
    override fun run(args: String): Single<List<SoundCloudTrackEntity>> = remote.getSimilarTracks(args)
}
