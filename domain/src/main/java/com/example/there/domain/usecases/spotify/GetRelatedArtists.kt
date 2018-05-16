package com.example.there.domain.usecases.spotify

import com.example.there.domain.common.Transformer
import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.domain.entities.spotify.ArtistEntity
import com.example.there.domain.repos.spotify.SpotifyRepository
import com.example.there.domain.usecases.UseCase
import com.example.there.domain.usecases.UseCaseParams
import io.reactivex.Observable

class GetRelatedArtists(transformer: Transformer<List<ArtistEntity>>,
                        private val repository: SpotifyRepository) : UseCase<List<ArtistEntity>>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<List<ArtistEntity>> {
        val accessToken = data?.get(UseCaseParams.PARAM_ACCESS_TOKEN) as? AccessTokenEntity
        val artistId = data?.get(UseCaseParams.PARAM_ARTIST_ID) as? String
        return if (accessToken != null && artistId != null) {
            repository.getRelatedArtists(accessToken, artistId)
        } else {
            Observable.error { IllegalArgumentException("Access token and artistId must be provided.") }
        }
    }

    fun execute(accessToken: AccessTokenEntity, artistId: String): Observable<List<ArtistEntity>> {
        val data = HashMap<String, Any?>().apply {
            put(UseCaseParams.PARAM_ACCESS_TOKEN, accessToken)
            put(UseCaseParams.PARAM_ARTIST_ID, artistId)
        }
        return execute(withData = data)
    }
}