package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricSingleTransformer
import com.example.there.domain.entity.spotify.SearchAllEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single

class SearchSpotify(
        transformer: SymmetricSingleTransformer<SearchAllEntity>,
        private val repository: ISpotifyRepository
) : SingleUseCase<SearchAllEntity>(transformer) {

    override fun createSingle(data: Map<String, Any?>?): Single<SearchAllEntity> {
        val query = data?.get(UseCaseParams.PARAM_SEARCH_ALL_QUERY) as? String
        val offset = data?.get(UseCaseParams.PARAM_OFFSET) as? Int
        return if (query != null && offset != null) {
            repository.searchAll(query, offset)
        } else {
            Single.error { IllegalArgumentException("AccessToken, query and offset must be provided.") }
        }
    }

    fun execute(query: String, offset: Int): Single<SearchAllEntity> {
        val data = HashMap<String, Any>().apply {
            put(UseCaseParams.PARAM_SEARCH_ALL_QUERY, query)
            put(UseCaseParams.PARAM_OFFSET, offset)
        }
        return execute(withData = data)
    }
}