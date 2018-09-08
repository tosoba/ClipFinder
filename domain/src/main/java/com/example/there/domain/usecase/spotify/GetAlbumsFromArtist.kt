package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricObservableTransformer
import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.ObservableUseCase
import io.reactivex.Observable

class GetAlbumsFromArtist(
        transformer: SymmetricObservableTransformer<List<AlbumEntity>>,
        private val repository: ISpotifyRepository
) : ObservableUseCase<List<AlbumEntity>>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<List<AlbumEntity>> {
        val accessToken = data?.get(UseCaseParams.PARAM_ACCESS_TOKEN) as? AccessTokenEntity
        val artistId = data?.get(UseCaseParams.PARAM_ARTIST_ID) as? String
        return if (accessToken != null && artistId != null) {
            repository.getAlbumsFromArtist(accessToken, artistId)
        } else {
            Observable.error { IllegalArgumentException("Access token and artistId must be provided.") }
        }
    }

    fun execute(accessToken: AccessTokenEntity, artistId: String): Observable<List<AlbumEntity>> {
        val data = HashMap<String, Any?>().apply {
            put(UseCaseParams.PARAM_ACCESS_TOKEN, accessToken)
            put(UseCaseParams.PARAM_ARTIST_ID, artistId)
        }
        return execute(withData = data)
    }
}