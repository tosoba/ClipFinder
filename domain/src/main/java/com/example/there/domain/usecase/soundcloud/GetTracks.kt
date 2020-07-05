package com.example.there.domain.usecase.soundcloud

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity
import com.example.there.domain.repo.soundcloud.ISoundCloudRemoteDataStore
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetTracks(
    schedulersProvider: UseCaseSchedulersProvider,
    private val remote: ISoundCloudRemoteDataStore
) : SingleUseCaseWithArgs<List<String>, List<SoundCloudTrackEntity>>(schedulersProvider) {
    override fun run(args: List<String>): Single<List<SoundCloudTrackEntity>> = remote.getTracks(args)
}
