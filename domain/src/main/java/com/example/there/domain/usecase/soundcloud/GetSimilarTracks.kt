package com.example.there.domain.usecase.soundcloud

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity
import com.example.there.domain.repo.soundcloud.ISoundCloudRepository
import com.example.there.domain.usecase.base.SingleUseCaseWithInput
import io.reactivex.Single
import javax.inject.Inject

class GetSimilarTracks @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISoundCloudRepository
) : SingleUseCaseWithInput<String, List<SoundCloudTrackEntity>>(schedulersProvider) {
    override fun createSingle(input: String): Single<List<SoundCloudTrackEntity>> = repository.getSimilarTracks(input)
}