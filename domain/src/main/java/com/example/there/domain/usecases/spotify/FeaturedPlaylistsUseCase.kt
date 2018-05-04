package com.example.there.domain.usecases.spotify

import com.example.there.domain.repos.spotify.SpotifyRepository
import com.example.there.domain.common.Transformer
import com.example.there.domain.usecases.UseCase
import com.example.there.domain.usecases.UseCaseParams
import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.domain.entities.spotify.PlaylistEntity
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