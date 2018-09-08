package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricSingleTransformer
import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.domain.entitypage.CategoryPlaylistsPage
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single
import java.lang.IllegalArgumentException

class GetPlaylistsForCategory(
        transformer: SymmetricSingleTransformer<CategoryPlaylistsPage>,
        private val repository: ISpotifyRepository
) : SingleUseCase<CategoryPlaylistsPage>(transformer) {

    override fun createSingle(data: Map<String, Any?>?): Single<CategoryPlaylistsPage> {
        val categoryId = data?.get(UseCaseParams.PARAM_CATEGORY_ID) as? String
        val accessToken = data?.get(UseCaseParams.PARAM_ACCESS_TOKEN) as? AccessTokenEntity
        val offset = data?.get(UseCaseParams.PARAM_OFFSET) as? Int
        return if (categoryId != null && accessToken != null && offset != null) {
            repository.getPlaylistsForCategory(accessToken, categoryId, offset)
        } else {
            Single.error { IllegalArgumentException("AccessToken and categoryId must be provided.") }
        }
    }

    fun execute(accessToken: AccessTokenEntity, categoryId: String, offset: Int): Single<CategoryPlaylistsPage> {
        val data = HashMap<String, Any>().apply {
            put(UseCaseParams.PARAM_CATEGORY_ID, categoryId)
            put(UseCaseParams.PARAM_ACCESS_TOKEN, accessToken)
            put(UseCaseParams.PARAM_OFFSET, offset)
        }
        return execute(withData = data)
    }
}