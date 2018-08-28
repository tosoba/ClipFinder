package com.example.there.domain.usecases.spotify

import com.example.there.domain.common.Transformer
import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.domain.entities.spotify.ArtistEntity
import com.example.there.domain.repos.spotify.ISpotifyRepository
import com.example.there.domain.usecases.UseCase
import com.example.there.domain.usecases.UseCaseParams
import io.reactivex.Observable

@Suppress("UNCHECKED_CAST")
class GetArtists(transformer: Transformer<List<ArtistEntity>>,
                 private val repository: ISpotifyRepository) : UseCase<List<ArtistEntity>>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<List<ArtistEntity>> {
        val accessToken = data?.get(UseCaseParams.PARAM_ACCESS_TOKEN) as? AccessTokenEntity
        val artistIds = data?.get(UseCaseParams.PARAM_ARTIST_IDS) as? List<String>
        return if (accessToken != null && artistIds != null) {
            repository.getArtists(accessToken, artistIds)
        } else {
            Observable.error { IllegalArgumentException("Access token and artistIds must be provided.") }
        }
    }

    fun execute(accessToken: AccessTokenEntity, artistIds: List<String>): Observable<List<ArtistEntity>> {
        val data = HashMap<String, Any?>().apply {
            put(UseCaseParams.PARAM_ACCESS_TOKEN, accessToken)
            put(UseCaseParams.PARAM_ARTIST_IDS, artistIds)
        }
        return execute(withData = data)
    }
}