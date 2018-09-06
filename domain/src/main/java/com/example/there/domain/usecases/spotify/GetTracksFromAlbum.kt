package com.example.there.domain.usecases.spotify

import com.example.there.domain.common.SymmetricSingleTransformer
import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.domain.pages.TracksPage
import com.example.there.domain.repos.spotify.ISpotifyRepository
import com.example.there.domain.usecases.UseCaseParams
import com.example.there.domain.usecases.base.SingleUseCase
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