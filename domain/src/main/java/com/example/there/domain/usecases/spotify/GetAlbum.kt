package com.example.there.domain.usecases.spotify

import com.example.there.domain.common.SymmetricObservableTransformer
import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.domain.entities.spotify.AlbumEntity
import com.example.there.domain.repos.spotify.ISpotifyRepository
import com.example.there.domain.usecases.UseCaseParams
import com.example.there.domain.usecases.base.ObservableUseCase
import io.reactivex.Observable
import java.lang.IllegalArgumentException

class GetAlbum(transformer: SymmetricObservableTransformer<AlbumEntity>,
               private val repository: ISpotifyRepository): ObservableUseCase<AlbumEntity>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<AlbumEntity> {
        val accessToken = data?.get(UseCaseParams.PARAM_ACCESS_TOKEN) as? AccessTokenEntity
        val albumId = data?.get(UseCaseParams.PARAM_ALBUM_ID) as? String
        return if (accessToken != null && albumId != null) {
            repository.getAlbum(accessToken, albumId)
        } else {
            Observable.error { IllegalArgumentException("Access token and albumId must be provided.") }
        }
    }

    fun execute(accessToken: AccessTokenEntity, albumId: String): Observable<AlbumEntity> {
        val data = HashMap<String, Any?>().apply {
            put(UseCaseParams.PARAM_ACCESS_TOKEN, accessToken)
            put(UseCaseParams.PARAM_ALBUM_ID, albumId)
        }
        return execute(withData = data)
    }
}