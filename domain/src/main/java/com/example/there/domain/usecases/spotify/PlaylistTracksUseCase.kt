package com.example.there.domain.usecases.spotify

import com.example.there.domain.common.Transformer
import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.domain.entities.spotify.TrackEntity
import com.example.there.domain.repos.spotify.SpotifyRepository
import com.example.there.domain.usecases.UseCase
import com.example.there.domain.usecases.UseCaseParams
import io.reactivex.Observable
import java.lang.IllegalArgumentException

class PlaylistTracksUseCase(transformer: Transformer<List<TrackEntity>>,
                            private val repository: SpotifyRepository) : UseCase<List<TrackEntity>>(transformer) {

    override fun createObservable(data: Map<String, Any>?): Observable<List<TrackEntity>> {
        val accessTokenEntity = data?.get(UseCaseParams.PARAM_ACCESS_TOKEN) as? AccessTokenEntity
        val playlistId = data?.get(UseCaseParams.PARAM_PLAYLIST_ID) as? String
        val userId = data?.get(UseCaseParams.PARAM_USER_ID) as? String
        return if (accessTokenEntity != null && playlistId != null && userId != null) {
            repository.getPlaylistTracks(accessTokenEntity, playlistId, userId)
        } else {
            Observable.error { IllegalArgumentException("Access token, playlistId and userId must be provided.") }
        }
    }

    fun getTracks(accessTokenEntity: AccessTokenEntity, playlistId: String, userId: String): Observable<List<TrackEntity>> {
        val data = HashMap<String, Any>().apply {
            put(UseCaseParams.PARAM_ACCESS_TOKEN, accessTokenEntity)
            put(UseCaseParams.PARAM_PLAYLIST_ID, playlistId)
            put(UseCaseParams.PARAM_USER_ID, userId)
        }
        return observable(withData = data)
    }
}