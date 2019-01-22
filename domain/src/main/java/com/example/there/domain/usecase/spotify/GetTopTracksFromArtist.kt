package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricSingleTransformer
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single

class GetTopTracksFromArtist(
        transformer: SymmetricSingleTransformer<List<TrackEntity>>,
        private val repository: ISpotifyRepository
) : SingleUseCase<List<TrackEntity>>(transformer) {

    override fun createSingle(data: Map<String, Any?>?): Single<List<TrackEntity>> {
        val artistId = data?.get(UseCaseParams.PARAM_ARTIST_ID) as? String
        return if (artistId != null) {
            repository.getTopTracksFromArtist(artistId)
        } else {
            Single.error { IllegalArgumentException("artistId must be provided.") }
        }
    }

    fun execute(artistId: String): Single<List<TrackEntity>> {
        val data = HashMap<String, Any?>().apply {
            put(UseCaseParams.PARAM_ARTIST_ID, artistId)
        }
        return execute(withData = data)
    }
}