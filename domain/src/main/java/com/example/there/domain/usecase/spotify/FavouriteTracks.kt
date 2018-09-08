package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricObservableTransformer
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.ObservableUseCase
import io.reactivex.Observable

class GetFavouriteTracks(transformer: SymmetricObservableTransformer<List<TrackEntity>>,
                         private val repository: ISpotifyRepository) : ObservableUseCase<List<TrackEntity>>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<List<TrackEntity>> = repository.getFavouriteTracks().toObservable()
}

class InsertTrack(transformer: SymmetricObservableTransformer<Unit>,
                  private val repository: ISpotifyRepository): ObservableUseCase<Unit>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<Unit> {
        val trackEntity = data?.get(UseCaseParams.PARAM_TRACK) as? TrackEntity
        return if (trackEntity != null) {
            repository.insertTrack(trackEntity).toObservable()
        } else {
            Observable.error { IllegalArgumentException("TrackEntity must be provided.") }
        }
    }

    fun execute(trackEntity: TrackEntity): Observable<Unit> {
        val data = HashMap<String, TrackEntity>().apply {
            put(UseCaseParams.PARAM_TRACK, trackEntity)
        }
        return execute(withData = data)
    }
}