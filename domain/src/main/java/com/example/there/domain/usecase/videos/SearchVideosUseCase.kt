package com.example.there.domain.usecase.videos

import com.example.there.domain.common.Transformer
import com.example.there.domain.common.UseCase
import com.example.there.domain.common.UseCaseParams
import com.example.there.domain.entities.videos.VideoEntity
import com.example.there.domain.repos.videos.VideosRepository
import io.reactivex.Observable
import java.lang.IllegalArgumentException

class SearchVideosUseCase(transformer: Transformer<List<VideoEntity>>,
                          private val repository: VideosRepository) : UseCase<List<VideoEntity>>(transformer) {

    override fun createObservable(data: Map<String, Any>?): Observable<List<VideoEntity>> {
        val query = data?.get(UseCaseParams.PARAM_VIDEO_QUERY) as? String
        return if (query != null) {
            repository.getVideos(query)
        } else {
            Observable.error { IllegalArgumentException("Query for videos search must be provided.") }
        }
    }

    fun getVideos(query: String): Observable<List<VideoEntity>> {
        val data = HashMap<String, String>().apply {
            put(UseCaseParams.PARAM_VIDEO_QUERY, query)
        }
        return observable(withData = data)
    }
}