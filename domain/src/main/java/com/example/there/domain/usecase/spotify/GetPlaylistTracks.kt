package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricSingleTransformer
import com.example.there.domain.entity.EntityPage
import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single
import java.lang.IllegalArgumentException

class GetPlaylistTracks(
        transformer: SymmetricSingleTransformer<EntityPage<TrackEntity>>,
        private val repository: ISpotifyRepository
) : SingleUseCase<EntityPage<TrackEntity>>(transformer) {

    override fun createSingle(data: Map<String, Any?>?): Single<EntityPage<TrackEntity>> {
        val accessTokenEntity = data?.get(UseCaseParams.PARAM_ACCESS_TOKEN) as? AccessTokenEntity
        val playlistId = data?.get(UseCaseParams.PARAM_PLAYLIST_ID) as? String
        val userId = data?.get(UseCaseParams.PARAM_USER_ID) as? String
        val offset = data?.get(UseCaseParams.PARAM_OFFSET) as? Int
        return if (accessTokenEntity != null && playlistId != null && userId != null && offset != null) {
            repository.getPlaylistTracks(accessTokenEntity, playlistId, userId, offset)
        } else {
            Single.error { IllegalArgumentException("Access token, playlistId and userId must be provided.") }
        }
    }

    fun execute(accessTokenEntity: AccessTokenEntity, playlistId: String, userId: String, offset: Int): Single<EntityPage<TrackEntity>> {
        val data = HashMap<String, Any>().apply {
            put(UseCaseParams.PARAM_ACCESS_TOKEN, accessTokenEntity)
            put(UseCaseParams.PARAM_PLAYLIST_ID, playlistId)
            put(UseCaseParams.PARAM_USER_ID, userId)
            put(UseCaseParams.PARAM_OFFSET, offset)
        }
        return execute(withData = data)
    }
}