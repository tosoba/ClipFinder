package com.example.there.domain.usecases.videos

import com.example.there.domain.common.Transformer
import com.example.there.domain.usecases.UseCase
import com.example.there.domain.usecases.UseCaseParams
import com.example.there.domain.entities.videos.VideoEntity
import com.example.there.domain.repos.videos.IVideosRepository
import io.reactivex.Observable
import java.lang.IllegalArgumentException

class SearchVideos(transformer: Transformer<Pair<String?, List<VideoEntity>>>,
                   private val repository: IVideosRepository) : UseCase<Pair<String?, List<VideoEntity>>>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<Pair<String?, List<VideoEntity>>> {
        val query = data?.get(UseCaseParams.PARAM_VIDEO_QUERY) as? String
        val pageToken = data?.get(UseCaseParams.PARAM_PAGE_TOKEN) as? String?
        return if (query != null) {
            repository.getVideos(query, pageToken)
        } else {
            Observable.error { IllegalArgumentException("Query for videos search must be provided.") }
        }
    }

    fun execute(query: String, pageToken: String? = null): Observable<Pair<String?, List<VideoEntity>>> {
        val data = HashMap<String, Any?>().apply {
            put(UseCaseParams.PARAM_VIDEO_QUERY, query)
            put(UseCaseParams.PARAM_PAGE_TOKEN, pageToken)
        }
        return execute(withData = data)
    }
}