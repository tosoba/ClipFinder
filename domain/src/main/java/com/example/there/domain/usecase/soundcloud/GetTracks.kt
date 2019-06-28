package com.example.there.domain.usecase.soundcloud

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity
import com.example.there.domain.repo.soundcloud.ISoundCloudRemoteDataStore
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single
import javax.inject.Inject

class GetTracks @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val remote: ISoundCloudRemoteDataStore
) : SingleUseCaseWithArgs<List<String>, List<SoundCloudTrackEntity>>(schedulersProvider) {
    override fun createSingle(args: List<String>): Single<List<SoundCloudTrackEntity>> = remote.getTracks(args)
}