package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricObservableTransformer
import com.example.there.domain.entity.EntityPage
import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.ObservableUseCase
import io.reactivex.Observable

class GetTracksFromAlbum(
        transformer: SymmetricObservableTransformer<EntityPage<TrackEntity>>,
        private val repository: ISpotifyRepository
) : ObservableUseCase<EntityPage<TrackEntity>>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<EntityPage<TrackEntity>> {
        val accessToken = data?.get(UseCaseParams.PARAM_ACCESS_TOKEN) as? AccessTokenEntity
        val albumId = data?.get(UseCaseParams.PARAM_ALBUM_ID) as? String
        return if (accessToken != null && albumId != null) {
            repository.getTracksFromAlbum(accessToken, albumId)
        } else {
            Observable.error { IllegalArgumentException("Access token and albumId must be provided.") }
        }
    }

    fun execute(accessToken: AccessTokenEntity, albumId: String): Observable<EntityPage<TrackEntity>> {
        val data = HashMap<String, Any?>().apply {
            put(UseCaseParams.PARAM_ACCESS_TOKEN, accessToken)
            put(UseCaseParams.PARAM_ALBUM_ID, albumId)
        }
        return execute(withData = data)
    }
}