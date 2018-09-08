package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricSingleTransformer
import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single

@Suppress("UNCHECKED_CAST")
class GetArtists(
        transformer: SymmetricSingleTransformer<List<ArtistEntity>>,
        private val repository: ISpotifyRepository
) : SingleUseCase<List<ArtistEntity>>(transformer) {

    override fun createSingle(data: Map<String, Any?>?): Single<List<ArtistEntity>> {
        val accessToken = data?.get(UseCaseParams.PARAM_ACCESS_TOKEN) as? AccessTokenEntity
        val artistIds = data?.get(UseCaseParams.PARAM_ARTIST_IDS) as? List<String>
        return if (accessToken != null && artistIds != null) {
            repository.getArtists(accessToken, artistIds)
        } else {
            Single.error { IllegalArgumentException("Access token and artistIds must be provided.") }
        }
    }

    fun execute(accessToken: AccessTokenEntity, artistIds: List<String>): Single<List<ArtistEntity>> {
        val data = HashMap<String, Any?>().apply {
            put(UseCaseParams.PARAM_ACCESS_TOKEN, accessToken)
            put(UseCaseParams.PARAM_ARTIST_IDS, artistIds)
        }
        return execute(withData = data)
    }
}