package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricSingleTransformer
import com.example.there.domain.entity.EntityPage
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single

class GetPlaylistTracks(
        transformer: SymmetricSingleTransformer<EntityPage<TrackEntity>>,
        private val repository: ISpotifyRepository
) : SingleUseCase<EntityPage<TrackEntity>>(transformer) {

    override fun createSingle(data: Map<String, Any?>?): Single<EntityPage<TrackEntity>> {
        val playlistId = data?.get(UseCaseParams.PARAM_PLAYLIST_ID) as? String
        val userId = data?.get(UseCaseParams.PARAM_USER_ID) as? String
        val offset = data?.get(UseCaseParams.PARAM_OFFSET) as? Int
        return if (playlistId != null && userId != null && offset != null) {
            repository.getPlaylistTracks(playlistId, userId, offset)
        } else {
            Single.error { IllegalArgumentException("Access token, playlistId and userId must be provided.") }
        }
    }

    fun execute(playlistId: String, userId: String, offset: Int): Single<EntityPage<TrackEntity>> {
        val data = HashMap<String, Any>().apply {
            put(UseCaseParams.PARAM_PLAYLIST_ID, playlistId)
            put(UseCaseParams.PARAM_USER_ID, userId)
            put(UseCaseParams.PARAM_OFFSET, offset)
        }
        return execute(withData = data)
    }
}