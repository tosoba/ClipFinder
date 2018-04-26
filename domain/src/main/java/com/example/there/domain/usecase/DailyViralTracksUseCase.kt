package com.example.there.domain.usecase

import com.example.there.domain.SpotifyRepository
import com.example.there.domain.common.Transformer
import com.example.there.domain.common.UseCase
import com.example.there.domain.common.UseCaseParams
import com.example.there.domain.entities.AccessTokenEntity
import com.example.there.domain.entities.TopTrackEntity
import io.reactivex.Observable
import java.lang.IllegalArgumentException

class DailyViralTracksUseCase(transformer: Transformer<List<TopTrackEntity>>,
                              private val repository: SpotifyRepository) : UseCase<List<TopTrackEntity>>(transformer) {

    override fun createObservable(data: Map<String, Any>?): Observable<List<TopTrackEntity>> {
        val accessToken = data?.get(UseCaseParams.PARAM_ACCESS_TOKEN) as? AccessTokenEntity
        return if (accessToken != null) {
            repository.getDailyViralTracks(accessToken)
        } else {
            Observable.error { IllegalArgumentException("AccessToken and track id must be provided.") }
        }
    }

    fun getTracks(accessTokenEntity: AccessTokenEntity): Observable<List<TopTrackEntity>> {
        val data = HashMap<String, Any>().apply {
            put(UseCaseParams.PARAM_ACCESS_TOKEN, accessTokenEntity)
        }
        return observable(withData = data)
    }
}