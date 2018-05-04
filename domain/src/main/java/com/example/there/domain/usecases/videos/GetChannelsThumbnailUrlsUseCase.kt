package com.example.there.domain.usecases.videos

import com.example.there.domain.common.Transformer
import com.example.there.domain.usecases.UseCase
import com.example.there.domain.usecases.UseCaseParams
import com.example.there.domain.entities.videos.VideoEntity
import com.example.there.domain.repos.videos.VideosRepository
import io.reactivex.Observable
import java.lang.IllegalArgumentException

class GetChannelsThumbnailUrlsUseCase(transformer: Transformer<List<String>>,
                                      private val repository: VideosRepository) : UseCase<List<String>>(transformer) {

    @Suppress("UNCHECKED_CAST")
    override fun createObservable(data: Map<String, Any>?): Observable<List<String>> {
        val videos = data?.get(UseCaseParams.PARAM_VIDEOS) as? List<VideoEntity>
        return if (videos != null) {
            repository.getChannelsThumbnailUrls(videos)
        } else {
            Observable.error { IllegalArgumentException("A list of videos must be provided.") }
        }
    }

    fun getUrls(videos: List<VideoEntity>): Observable<List<String>> {
        val data = HashMap<String, Any>().apply {
            put(UseCaseParams.PARAM_VIDEOS, videos)
        }
        return observable(withData = data)
    }
}