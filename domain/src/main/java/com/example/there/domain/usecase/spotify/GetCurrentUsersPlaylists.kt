package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricSingleTransformer
import com.example.there.domain.entity.EntityPage
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single

class GetCurrentUsersPlaylists(
        transformer: SymmetricSingleTransformer<EntityPage<PlaylistEntity>>,
        private val repository: ISpotifyRepository
) : SingleUseCase<EntityPage<PlaylistEntity>>(transformer) {

    override fun createSingle(data: Map<String, Any?>?): Single<EntityPage<PlaylistEntity>> {
        val offset = data?.get(UseCaseParams.PARAM_OFFSET) as? Int
        return if (offset != null) {
            repository.getCurrentUsersPlaylists(offset)
        } else {
            Single.error { IllegalArgumentException("offset must be provided.") }
        }
    }

    fun execute(
            offset: Int
    ): Single<EntityPage<PlaylistEntity>> = execute(withData = mapOf(
            UseCaseParams.PARAM_OFFSET to offset
    ))
}