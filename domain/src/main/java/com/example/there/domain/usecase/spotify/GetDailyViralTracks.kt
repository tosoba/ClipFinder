package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricObservableTransformer
import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.domain.entity.spotify.TopTrackEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.ObservableUseCase
import io.reactivex.Observable
import java.lang.IllegalArgumentException

class GetDailyViralTracks(
        transformer: SymmetricObservableTransformer<List<TopTrackEntity>>,
        private val repository: ISpotifyRepository
) : ObservableUseCase<List<TopTrackEntity>>(transformer) {

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