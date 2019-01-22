package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricSingleTransformer
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
        val artistIds = data?.get(UseCaseParams.PARAM_ARTIST_IDS) as? List<String>
        return if (artistIds != null) {
            repository.getArtists(artistIds)
        } else {
            Single.error { IllegalArgumentException("artistIds must be provided.") }
        }
    }

    fun execute(artistIds: List<String>): Single<List<ArtistEntity>> {
        val data = HashMap<String, Any?>().apply {
            put(UseCaseParams.PARAM_ARTIST_IDS, artistIds)
        }
        return execute(withData = data)
    }
}