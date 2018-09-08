package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricSingleTransformer
import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.domain.entitypage.TracksPage
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single

class GetTracksFromAlbum(
        transformer: SymmetricSingleTransformer<TracksPage>,
        private val repository: ISpotifyRepository
) : SingleUseCase<TracksPage>(transformer) {

    override fun createSingle(data: Map<String, Any?>?): Single<TracksPage> {
        val accessToken = data?.get(UseCaseParams.PARAM_ACCESS_TOKEN) as? AccessTokenEntity
        val albumId = data?.get(UseCaseParams.PARAM_ALBUM_ID) as? String
        val offset = data?.get(UseCaseParams.PARAM_OFFSET) as? Int
        return if (accessToken != null && albumId != null && offset != null) {
            repository.getTracksFromAlbum(accessToken, albumId, offset)
        } else {
            Single.error { IllegalArgumentException("Access token and albumId must be provided.") }
        }
    }

    fun execute(accessToken: AccessTokenEntity, albumId: String, offset: Int): Single<TracksPage> {
        val data = HashMap<String, Any?>().apply {
            put(UseCaseParams.PARAM_ACCESS_TOKEN, accessToken)
            put(UseCaseParams.PARAM_ALBUM_ID, albumId)
            put(UseCaseParams.PARAM_OFFSET, offset)
        }
        return execute(withData = data)
    }
}