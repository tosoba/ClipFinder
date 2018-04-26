package com.example.there.domain.usecase

import com.example.there.domain.SpotifyRepository
import com.example.there.domain.common.Transformer
import com.example.there.domain.common.UseCase
import com.example.there.domain.common.UseCaseParams
import com.example.there.domain.entities.AccessTokenEntity
import com.example.there.domain.entities.PlaylistEntity
import io.reactivex.Observable
import java.lang.IllegalArgumentException

class FeaturedPlaylistsUseCase(transformer: Transformer<List<PlaylistEntity>>,
                               private val repository: SpotifyRepository) : UseCase<List<PlaylistEntity>>(transformer) {

    override fun createObservable(data: Map<String, Any>?): Observable<List<PlaylistEntity>> {
        val accessToken = data?.get(UseCaseParams.PARAM_ACCESS_TOKEN) as? AccessTokenEntity
        return if (accessToken != null) {
            repository.getFeaturedPlaylists(accessToken)
        } else {
            Observable.error { IllegalArgumentException("AccessToken must be provided.") }
        }
    }

    fun getFeaturedPlaylists(accessToken: AccessTokenEntity): Observable<List<PlaylistEntity>> {
        val data = HashMap<String, Any>().apply { put(UseCaseParams.PARAM_ACCESS_TOKEN, accessToken) }
        return observable(withData = data)
    }
}