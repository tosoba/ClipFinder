package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricObservableTransformer
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.ObservableUseCase
import io.reactivex.Observable

class GetSimilarTracks(
        transformer: SymmetricObservableTransformer<List<TrackEntity>>,
        private val repository: ISpotifyRepository
) : ObservableUseCase<List<TrackEntity>>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<List<TrackEntity>> {
        val trackId = data?.get(UseCaseParams.PARAM_TRACK_ID) as? String
        return if (trackId != null) {
            repository.getSimilarTracks(trackId)
        } else {
            Observable.error { IllegalArgumentException("trackId must be provided.") }
        }
    }

    fun execute(trackId: String): Observable<List<TrackEntity>> {
        val data = HashMap<String, Any?>().apply {
            put(UseCaseParams.PARAM_TRACK_ID, trackId)
        }
        return execute(withData = data)
    }
}