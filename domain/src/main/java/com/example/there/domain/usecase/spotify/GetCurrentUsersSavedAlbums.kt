package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricSingleTransformer
import com.example.there.domain.entity.EntityPage
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single

class GetCurrentUsersSavedAlbums(
        transformer: SymmetricSingleTransformer<EntityPage<AlbumEntity>>,
        private val repository: ISpotifyRepository
) : SingleUseCase<EntityPage<AlbumEntity>>(transformer) {

    override fun createSingle(data: Map<String, Any?>?): Single<EntityPage<AlbumEntity>> {
        val offset = data?.get(UseCaseParams.PARAM_OFFSET) as? Int
        return if (offset != null) {
            repository.getCurrentUsersSavedAlbums(offset)
        } else {
            Single.error { IllegalArgumentException("offset must be provided.") }
        }
    }

    fun execute(
            offset: Int
    ): Single<EntityPage<AlbumEntity>> = execute(withData = mapOf(
            UseCaseParams.PARAM_OFFSET to offset
    ))
}