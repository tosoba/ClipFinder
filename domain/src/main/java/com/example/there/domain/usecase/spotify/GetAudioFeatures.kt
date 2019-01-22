package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricSingleTransformer
import com.example.there.domain.entity.spotify.AudioFeaturesEntity
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single

class GetAudioFeatures(
        transformer: SymmetricSingleTransformer<AudioFeaturesEntity>,
        private val repository: ISpotifyRepository
) : SingleUseCase<AudioFeaturesEntity>(transformer) {

    override fun createSingle(data: Map<String, Any?>?): Single<AudioFeaturesEntity> {
        val track = data?.get(UseCaseParams.PARAM_TRACK) as? TrackEntity
        return if (track != null) {
            repository.getAudioFeatures(track)
        } else {
            Single.error { IllegalArgumentException("track must be provided.") }
        }
    }

    fun execute(track: TrackEntity): Single<AudioFeaturesEntity> {
        val data = HashMap<String, Any?>().apply {
            put(UseCaseParams.PARAM_TRACK, track)
        }
        return execute(withData = data)
    }
}