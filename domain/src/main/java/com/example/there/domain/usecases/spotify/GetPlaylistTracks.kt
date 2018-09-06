package com.example.there.domain.usecases.spotify

import com.example.there.domain.common.SymmetricSingleTransformer
import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.domain.pages.TracksPage
import com.example.there.domain.repos.spotify.ISpotifyRepository
import com.example.there.domain.usecases.UseCaseParams
import com.example.there.domain.usecases.base.SingleUseCase
import io.reactivex.Single
import java.lang.IllegalArgumentException

class GetPlaylistTracks(
        transformer: SymmetricSingleTransformer<TracksPage>,
        private val repository: ISpotifyRepository
) : SingleUseCase<TracksPage>(transformer) {

    override fun createSingle(data: Map<String, Any?>?): Single<TracksPage> {
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

    fun execute(accessTokenEntity: AccessTokenEntity, playlistId: String, userId: String, offset: Int): Single<TracksPage> {
        val data = HashMap<String, Any>().apply {
            put(UseCaseParams.PARAM_ACCESS_TOKEN, accessTokenEntity)
            put(UseCaseParams.PARAM_PLAYLIST_ID, playlistId)
            put(UseCaseParams.PARAM_USER_ID, userId)
            put(UseCaseParams.PARAM_OFFSET, offset)
        }
        return execute(withData = data)
    }
}