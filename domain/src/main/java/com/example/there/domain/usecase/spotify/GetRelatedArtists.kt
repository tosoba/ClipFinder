package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricSingleTransformer
import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single

class GetRelatedArtists(
        transformer: SymmetricSingleTransformer<List<ArtistEntity>>,
        private val repository: ISpotifyRepository
) : SingleUseCase<List<ArtistEntity>>(transformer) {

    override fun createSingle(data: Map<String, Any?>?): Single<List<ArtistEntity>> {
        val accessToken = data?.get(UseCaseParams.PARAM_ACCESS_TOKEN) as? AccessTokenEntity
        val artistId = data?.get(UseCaseParams.PARAM_ARTIST_ID) as? String
        return if (accessToken != null && artistId != null) {
            repository.getRelatedArtists(accessToken, artistId)
        } else {
            Single.error { IllegalArgumentException("Access token and artistId must be provided.") }
        }
    }

    fun execute(accessToken: AccessTokenEntity, artistId: String): Single<List<ArtistEntity>> {
        val data = HashMap<String, Any?>().apply {
            put(UseCaseParams.PARAM_ACCESS_TOKEN, accessToken)
            put(UseCaseParams.PARAM_ARTIST_ID, artistId)
        }
        return execute(withData = data)
    }
}