package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricSingleTransformer
import com.example.there.domain.entity.EntityPage
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single

class GetPlaylistsForCategory(
        transformer: SymmetricSingleTransformer<EntityPage<PlaylistEntity>>,
        private val repository: ISpotifyRepository
) : SingleUseCase<EntityPage<PlaylistEntity>>(transformer) {

    override fun createSingle(data: Map<String, Any?>?): Single<EntityPage<PlaylistEntity>> {
        val categoryId = data?.get(UseCaseParams.PARAM_CATEGORY_ID) as? String

        val offset = data?.get(UseCaseParams.PARAM_OFFSET) as? Int
        return if (categoryId != null && offset != null) {
            repository.getPlaylistsForCategory(categoryId, offset)
        } else {
            Single.error { IllegalArgumentException("categoryId must be provided.") }
        }
    }

    fun execute(categoryId: String, offset: Int): Single<EntityPage<PlaylistEntity>> {
        val data = HashMap<String, Any>().apply {
            put(UseCaseParams.PARAM_CATEGORY_ID, categoryId)

            put(UseCaseParams.PARAM_OFFSET, offset)
        }
        return execute(withData = data)
    }
}