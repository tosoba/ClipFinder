package com.example.there.domain.usecases.spotify

import com.example.there.domain.common.Transformer
import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.domain.usecases.UseCase
import com.example.there.domain.entities.spotify.PlaylistEntity
import com.example.there.domain.repos.spotify.SpotifyRepository
import com.example.there.domain.usecases.UseCaseParams
import io.reactivex.Observable
import java.lang.IllegalArgumentException

class PlaylistsForCategoryUseCase(transformer: Transformer<List<PlaylistEntity>>,
                                  private val repository: SpotifyRepository) : UseCase<List<PlaylistEntity>>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<List<PlaylistEntity>> {
        val categoryId = data?.get(UseCaseParams.PARAM_CATEGORY_ID) as? String
        val accessToken = data?.get(UseCaseParams.PARAM_ACCESS_TOKEN) as? AccessTokenEntity
        return if (categoryId != null && accessToken != null) {
            repository.getPlaylistsForCategory(accessToken, categoryId)
        } else {
            Observable.error { IllegalArgumentException("AccessToken and categoryId must be provided.") }
        }
    }

    fun execute(accessToken: AccessTokenEntity, categoryId: String): Observable<List<PlaylistEntity>> {
        val data = HashMap<String, Any>().apply {
            put(UseCaseParams.PARAM_CATEGORY_ID, categoryId)
            put(UseCaseParams.PARAM_ACCESS_TOKEN, accessToken)
        }
        return execute(withData = data)
    }
}