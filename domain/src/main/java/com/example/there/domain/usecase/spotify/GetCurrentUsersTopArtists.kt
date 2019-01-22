package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricSingleTransformer
import com.example.there.domain.entity.EntityPage
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single

class GetCurrentUsersTopArtists(
        transformer: SymmetricSingleTransformer<EntityPage<ArtistEntity>>,
        private val repository: ISpotifyRepository
) : SingleUseCase<EntityPage<ArtistEntity>>(transformer) {

    override fun createSingle(data: Map<String, Any?>?): Single<EntityPage<ArtistEntity>> {
        val offset = data?.get(UseCaseParams.PARAM_OFFSET) as? Int
        return if (offset != null) {
            repository.getCurrentUsersTopArtists(offset)
        } else {
            Single.error { IllegalArgumentException("offset must be provided.") }
        }
    }

    fun execute(
            offset: Int
    ): Single<EntityPage<ArtistEntity>> = execute(withData = mapOf(
            UseCaseParams.PARAM_OFFSET to offset
    ))
}