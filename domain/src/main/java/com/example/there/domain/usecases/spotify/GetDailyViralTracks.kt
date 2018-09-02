package com.example.there.domain.usecases.spotify

import com.example.there.domain.common.SymmetricObservableTransformer
import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.domain.entities.spotify.TopTrackEntity
import com.example.there.domain.repos.spotify.ISpotifyRepository
import com.example.there.domain.usecases.UseCaseParams
import com.example.there.domain.usecases.base.ObservableUseCase
import io.reactivex.Observable
import java.lang.IllegalArgumentException

class GetDailyViralTracks(transformer: SymmetricObservableTransformer<List<TopTrackEntity>>,
                          private val repository: ISpotifyRepository) : ObservableUseCase<List<TopTrackEntity>>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<List<TopTrackEntity>> {
        val accessToken = data?.get(UseCaseParams.PARAM_ACCESS_TOKEN) as? AccessTokenEntity
        return if (accessToken != null) {
            repository.getDailyViralTracks(accessToken)
        } else {
            Observable.error { IllegalArgumentException("AccessToken and track id must be provided.") }
        }
    }

    fun execute(accessTokenEntity: AccessTokenEntity): Observable<List<TopTrackEntity>> {
        val data = HashMap<String, Any>().apply {
            put(UseCaseParams.PARAM_ACCESS_TOKEN, accessTokenEntity)
        }
        return execute(withData = data)
    }
}