package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricSingleTransformer
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single

class GetAlbum(
        transformer: SymmetricSingleTransformer<AlbumEntity>,
        private val repository: ISpotifyRepository
) : SingleUseCase<AlbumEntity>(transformer) {

    override fun createSingle(data: Map<String, Any?>?): Single<AlbumEntity> {
        val albumId = data?.get(UseCaseParams.PARAM_ALBUM_ID) as? String
        return if (albumId != null) {
            repository.getAlbum(albumId)
        } else {
            Single.error { IllegalArgumentException("albumId must be provided.") }
        }
    }

    fun execute(albumId: String): Single<AlbumEntity> {
        val data = HashMap<String, Any?>().apply {
            put(UseCaseParams.PARAM_ALBUM_ID, albumId)
        }
        return execute(withData = data)
    }
}