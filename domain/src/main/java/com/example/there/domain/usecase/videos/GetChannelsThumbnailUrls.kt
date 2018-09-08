package com.example.there.domain.usecase.videos

import com.example.there.domain.common.SymmetricSingleTransformer
import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.repo.videos.IVideosRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single
import java.lang.IllegalArgumentException

class GetChannelsThumbnailUrls(
        transformer: SymmetricSingleTransformer<List<Pair<Int, String>>>,
        private val repository: IVideosRepository
) : SingleUseCase<List<Pair<Int, String>>>(transformer) {

    @Suppress("UNCHECKED_CAST")
    override fun createSingle(data: Map<String, Any?>?): Single<List<Pair<Int, String>>> {
        val videos = data?.get(UseCaseParams.PARAM_VIDEOS) as? List<VideoEntity>
        return if (videos != null) {
            repository.getChannelsThumbnailUrls(videos)
        } else {
            Single.error { IllegalArgumentException("A list of videos must be provided.") }
        }
    }

    fun execute(videos: List<VideoEntity>): Single<List<Pair<Int, String>>> {
        val data = HashMap<String, Any?>().apply {
            put(UseCaseParams.PARAM_VIDEOS, videos)
        }
        return execute(withData = data)
    }
}