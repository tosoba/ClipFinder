package com.example.there.domain.usecases.spotify

import com.example.there.domain.common.Transformer
import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.domain.entities.spotify.TrackEntity
import com.example.there.domain.repos.spotify.SpotifyRepository
import com.example.there.domain.usecases.UseCase
import com.example.there.domain.usecases.UseCaseParams
import io.reactivex.Observable

class GetTracksFromAlbum(transformer: Transformer<List<TrackEntity>>,
                         private val repository: SpotifyRepository) : UseCase<List<TrackEntity>>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<List<TrackEntity>> {
        val accessToken = data?.get(UseCaseParams.PARAM_ACCESS_TOKEN) as? AccessTokenEntity
        val albumId = data?.get(UseCaseParams.PARAM_ALBUM_ID) as? String
        return if (accessToken != null && albumId != null) {
            repository.getTracksFromAlbum(accessToken, albumId)
        } else {
            Observable.error { IllegalArgumentException("Access token and albumId must be provided.") }
        }
    }

    fun execute(accessToken: AccessTokenEntity, albumId: String): Observable<List<TrackEntity>> {
        val data = HashMap<String, Any?>().apply {
            put(UseCaseParams.PARAM_ACCESS_TOKEN, accessToken)
            put(UseCaseParams.PARAM_ALBUM_ID, albumId)
        }
        return execute(withData = data)
    }
}