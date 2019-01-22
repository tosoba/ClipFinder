package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricSingleTransformer
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
        val artistId = data?.get(UseCaseParams.PARAM_ARTIST_ID) as? String
        return if (artistId != null) {
            repository.getRelatedArtists(artistId)
        } else {
            Single.error { IllegalArgumentException("artistId must be provided.") }
        }
    }

    fun execute(artistId: String): Single<List<ArtistEntity>> {
        val data = HashMap<String, Any?>().apply {
            put(UseCaseParams.PARAM_ARTIST_ID, artistId)
        }
        return execute(withData = data)
    }
}