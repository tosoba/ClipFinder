package com.example.there.domain.usecases.videos

import com.example.there.domain.common.Transformer
import com.example.there.domain.entities.videos.VideoEntity
import com.example.there.domain.repos.videos.IVideosRepository
import com.example.there.domain.usecases.UseCase
import com.example.there.domain.usecases.UseCaseParams
import io.reactivex.Observable
import java.lang.IllegalArgumentException

class SearchRelatedVideos(transformer: Transformer<Pair<String?, List<VideoEntity>>>,
                          private val repository: IVideosRepository) : UseCase<Pair<String?, List<VideoEntity>>>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<Pair<String?, List<VideoEntity>>> {
        val videoId = data?.get(UseCaseParams.PARAM_VIDEO_ID) as? String
        val pageToken = data?.get(UseCaseParams.PARAM_PAGE_TOKEN) as? String?
        return if (videoId != null) {
            repository.getRelatedVideos(videoId, pageToken)
        } else {
            Observable.error { IllegalArgumentException("VideoId and optionally page token must be provided.") }
        }
    }

    fun execute(videoId: String, pageToken: String?): Observable<Pair<String?, List<VideoEntity>>> {
        val data = HashMap<String, Any?>().apply {
            put(UseCaseParams.PARAM_VIDEO_ID, videoId)
            put(UseCaseParams.PARAM_PAGE_TOKEN, pageToken)
        }
        return execute(withData = data)
    }
}