package com.example.there.domain.usecases.spotify

import com.example.there.domain.common.Transformer
import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.domain.entities.spotify.TrackEntity
import com.example.there.domain.repos.spotify.ISpotifyRepository
import com.example.there.domain.usecases.UseCase
import com.example.there.domain.usecases.UseCaseParams
import io.reactivex.Observable

class GetSimilarTracks(transformer: Transformer<List<TrackEntity>>,
                       private val repository: ISpotifyRepository) : UseCase<List<TrackEntity>>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<List<TrackEntity>> {
        val accessToken = data?.get(UseCaseParams.PARAM_ACCESS_TOKEN) as? AccessTokenEntity
        val trackId = data?.get(UseCaseParams.PARAM_TRACK_ID) as? String
        return if (accessToken != null && trackId != null) {
            repository.getSimilarTracks(accessToken, trackId)
        } else {
            Observable.error { IllegalArgumentException("Access token and trackId must be provided.") }
        }
    }

    fun execute(accessToken: AccessTokenEntity, trackId: String): Observable<List<TrackEntity>> {
        val data = HashMap<String, Any?>().apply {
            put(UseCaseParams.PARAM_ACCESS_TOKEN, accessToken)
            put(UseCaseParams.PARAM_TRACK_ID, trackId)
        }
        return execute(withData = data)
    }
}