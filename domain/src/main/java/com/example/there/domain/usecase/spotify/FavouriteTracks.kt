package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricFlowableTransformer
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.CompletableUseCase
import com.example.there.domain.usecase.base.FlowableUseCase
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.Flowable

class GetFavouriteTracks(
        transformer: SymmetricFlowableTransformer<List<TrackEntity>>,
        private val repository: ISpotifyRepository
) : FlowableUseCase<List<TrackEntity>>(transformer) {

    override fun createFlowable(data: Map<String, Any?>?): Flowable<List<TrackEntity>> = repository.getFavouriteTracks()
}

class InsertTrack(
        transformer: CompletableTransformer,
        private val repository: ISpotifyRepository
) : CompletableUseCase(transformer) {

    override fun createCompletable(data: Map<String, Any?>?): Completable {
        val trackEntity = data?.get(UseCaseParams.PARAM_TRACK) as? TrackEntity
        return if (trackEntity != null) {
            repository.insertTrack(trackEntity)
        } else {
            Completable.error { IllegalArgumentException("TrackEntity must be provided.") }
        }
    }

    fun execute(trackEntity: TrackEntity): Completable {
        val data = HashMap<String, TrackEntity>().apply {
            put(UseCaseParams.PARAM_TRACK, trackEntity)
        }
        return execute(withData = data)
    }
}