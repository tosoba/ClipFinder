package com.example.there.domain.usecases.spotify

import com.example.there.domain.common.SymmetricObservableTransformer
import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.domain.entities.spotify.TrackEntity
import com.example.there.domain.repos.spotify.ISpotifyRepository
import com.example.there.domain.usecases.UseCaseParams
import com.example.there.domain.usecases.base.ObservableUseCase
import io.reactivex.Observable
import java.lang.IllegalArgumentException

class GetPlaylistTracks(transformer: SymmetricObservableTransformer<List<TrackEntity>>,
                        private val repository: ISpotifyRepository) : ObservableUseCase<List<TrackEntity>>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<List<TrackEntity>> {
        val accessTokenEntity = data?.get(UseCaseParams.PARAM_ACCESS_TOKEN) as? AccessTokenEntity
        val playlistId = data?.get(UseCaseParams.PARAM_PLAYLIST_ID) as? String
        val userId = data?.get(UseCaseParams.PARAM_USER_ID) as? String
        return if (accessTokenEntity != null && playlistId != null && userId != null) {
            repository.getPlaylistTracks(accessTokenEntity, playlistId, userId)
        } else {
            Observable.error { IllegalArgumentException("Access token, playlistId and userId must be provided.") }
        }
    }

    fun execute(accessTokenEntity: AccessTokenEntity, playlistId: String, userId: String): Observable<List<TrackEntity>> {
        val data = HashMap<String, Any>().apply {
            put(UseCaseParams.PARAM_ACCESS_TOKEN, accessTokenEntity)
            put(UseCaseParams.PARAM_PLAYLIST_ID, playlistId)
            put(UseCaseParams.PARAM_USER_ID, userId)
        }
        return execute(withData = data)
    }
}