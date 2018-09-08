package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricObservableTransformer
import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.ObservableUseCase
import io.reactivex.Observable
import java.lang.IllegalArgumentException

class GetFeaturedPlaylists(
        transformer: SymmetricObservableTransformer<List<PlaylistEntity>>,
        private val repository: ISpotifyRepository
) : ObservableUseCase<List<PlaylistEntity>>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<List<PlaylistEntity>> {
        val accessToken = data?.get(UseCaseParams.PARAM_ACCESS_TOKEN) as? AccessTokenEntity
        return if (accessToken != null) {
            repository.getFeaturedPlaylists(accessToken)
        } else {
            Observable.error { IllegalArgumentException("AccessToken must be provided.") }
        }
    }

    fun execute(accessToken: AccessTokenEntity): Observable<List<PlaylistEntity>> {
        val data = HashMap<String, Any>().apply { put(UseCaseParams.PARAM_ACCESS_TOKEN, accessToken) }
        return execute(withData = data)
    }
}