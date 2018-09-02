package com.example.there.domain.usecases.videos

import com.example.there.domain.common.SymmetricSingleTransformer
import com.example.there.domain.entities.videos.VideoEntity
import com.example.there.domain.repos.videos.IVideosRepository
import com.example.there.domain.usecases.UseCaseParams
import com.example.there.domain.usecases.base.SingleUseCase
import io.reactivex.Single
import java.lang.IllegalArgumentException

class SearchRelatedVideos(
        transformer: SymmetricSingleTransformer<List<VideoEntity>>,
        private val repository: IVideosRepository
) : SingleUseCase<List<VideoEntity>>(transformer) {

    override fun createSingle(data: Map<String, Any?>?): Single<List<VideoEntity>> {
        val videoId = data?.get(UseCaseParams.PARAM_VIDEO_ID) as? String
        val loadMore = data?.get(UseCaseParams.PARAM_LOAD_MORE) as? Boolean
        return if (videoId != null) {
            if (loadMore != null && loadMore) repository.getMoreRelatedVideos(videoId)
            else repository.getRelatedVideos(videoId)
        } else {
            Single.error { IllegalArgumentException("VideoId and optionally page token must be provided.") }
        }
    }

    fun execute(videoId: String, loadMore: Boolean): Single<List<VideoEntity>> {
        val data = HashMap<String, Any?>().apply {
            put(UseCaseParams.PARAM_VIDEO_ID, videoId)
            put(UseCaseParams.PARAM_LOAD_MORE, loadMore)
        }
        return execute(withData = data)
    }
}