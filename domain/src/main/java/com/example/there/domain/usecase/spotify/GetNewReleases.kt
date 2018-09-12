package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricSingleTransformer
import com.example.there.domain.entity.EntityPage
import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single

class GetNewReleases(
        transformer: SymmetricSingleTransformer<EntityPage<AlbumEntity>>,
        private val repository: ISpotifyRepository
) : SingleUseCase<EntityPage<AlbumEntity>>(transformer) {

    override fun createSingle(data: Map<String, Any?>?): Single<EntityPage<AlbumEntity>> {
        val accessToken = data?.get(UseCaseParams.PARAM_ACCESS_TOKEN) as? AccessTokenEntity
        val offset = data?.get(UseCaseParams.PARAM_OFFSET) as? Int
        return if (accessToken != null && offset != null) {
            repository.getNewReleases(accessToken, offset)
        } else {
            Single.error { IllegalArgumentException("Access token and offset must be provided.") }
        }
    }

    fun execute(accessToken: AccessTokenEntity, offset: Int): Single<EntityPage<AlbumEntity>> {
        val data = HashMap<String, Any?>().apply {
            put(UseCaseParams.PARAM_ACCESS_TOKEN, accessToken)
            put(UseCaseParams.PARAM_OFFSET, offset)
        }
        return execute(withData = data)
    }
}
