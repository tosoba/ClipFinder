package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricSingleTransformer
import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single
import java.lang.IllegalArgumentException

class GetAlbum(
        transformer: SymmetricSingleTransformer<AlbumEntity>,
        private val repository: ISpotifyRepository
) : SingleUseCase<AlbumEntity>(transformer) {

    override fun createSingle(data: Map<String, Any?>?): Single<AlbumEntity> {
        val accessToken = data?.get(UseCaseParams.PARAM_ACCESS_TOKEN) as? AccessTokenEntity
        val albumId = data?.get(UseCaseParams.PARAM_ALBUM_ID) as? String
        return if (accessToken != null && albumId != null) {
            repository.getAlbum(accessToken, albumId)
        } else {
            Single.error { IllegalArgumentException("Access token and albumId must be provided.") }
        }
    }

    fun execute(accessToken: AccessTokenEntity, albumId: String): Single<AlbumEntity> {
        val data = HashMap<String, Any?>().apply {
            put(UseCaseParams.PARAM_ACCESS_TOKEN, accessToken)
            put(UseCaseParams.PARAM_ALBUM_ID, albumId)
        }
        return execute(withData = data)
    }
}