package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricSingleTransformer
import com.example.there.domain.entity.EntityPage
import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single

class GetCurrentUsersSavedTracks(
        transformer: SymmetricSingleTransformer<EntityPage<TrackEntity>>,
        private val repository: ISpotifyRepository
) : SingleUseCase<EntityPage<TrackEntity>>(transformer) {

    override fun createSingle(data: Map<String, Any?>?): Single<EntityPage<TrackEntity>> {
        val accessToken = data?.get(UseCaseParams.PARAM_ACCESS_TOKEN) as? AccessTokenEntity
        val offset = data?.get(UseCaseParams.PARAM_OFFSET) as? Int
        return if (accessToken != null && offset != null) {
            repository.getCurrentUsersSavedTracks(accessToken, offset)
        } else {
            Single.error { IllegalArgumentException("Access token and offset must be provided.") }
        }
    }

    fun execute(
            accessToken: AccessTokenEntity,
            offset: Int
    ): Single<EntityPage<TrackEntity>> = execute(withData = mapOf(
            UseCaseParams.PARAM_ACCESS_TOKEN to accessToken,
            UseCaseParams.PARAM_OFFSET to offset
    ))
}