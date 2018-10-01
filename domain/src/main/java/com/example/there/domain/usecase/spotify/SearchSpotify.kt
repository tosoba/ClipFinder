package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricSingleTransformer
import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.domain.entity.spotify.SearchAllEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single
import java.lang.IllegalArgumentException

class SearchSpotify(
        transformer: SymmetricSingleTransformer<SearchAllEntity>,
        private val repository: ISpotifyRepository
) : SingleUseCase<SearchAllEntity>(transformer) {

    override fun createSingle(data: Map<String, Any?>?): Single<SearchAllEntity> {
        val accessToken = data?.get(UseCaseParams.PARAM_ACCESS_TOKEN) as? AccessTokenEntity
        val query = data?.get(UseCaseParams.PARAM_SEARCH_ALL_QUERY) as? String
        val offset = data?.get(UseCaseParams.PARAM_OFFSET) as? Int
        return if (accessToken != null && query != null && offset != null) {
            repository.searchAll(accessToken, query, offset)
        } else {
            Single.error { IllegalArgumentException("AccessToken, query and offset must be provided.") }
        }
    }

    fun execute(accessTokenEntity: AccessTokenEntity, query: String, offset: Int): Single<SearchAllEntity> {
        val data = HashMap<String, Any>().apply {
            put(UseCaseParams.PARAM_ACCESS_TOKEN, accessTokenEntity)
            put(UseCaseParams.PARAM_SEARCH_ALL_QUERY, query)
            put(UseCaseParams.PARAM_OFFSET, offset)
        }
        return execute(withData = data)
    }
}