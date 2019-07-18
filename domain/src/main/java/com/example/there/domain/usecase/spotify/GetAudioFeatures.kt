package com.example.there.domain.usecase.spotify

import com.example.core.model.Resource
import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.spotify.AudioFeaturesEntity
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetAudioFeatures(
        schedulersProvider: UseCaseSchedulersProvider,
        private val remote: ISpotifyRemoteDataStore
) : SingleUseCaseWithArgs<TrackEntity, Resource<AudioFeaturesEntity>>(schedulersProvider) {
    override fun run(args: TrackEntity): Single<Resource<AudioFeaturesEntity>> = remote.getAudioFeatures(args)
}