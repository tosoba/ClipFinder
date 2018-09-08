package com.example.there.domain.usecase.videos

import com.example.there.domain.common.SymmetricSingleTransformer
import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.repo.videos.IVideosRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single
import java.lang.IllegalArgumentException

class SearchVideos(
        transformer: SymmetricSingleTransformer<List<VideoEntity>>,
        private val repository: IVideosRepository
) : SingleUseCase<List<VideoEntity>>(transformer) {

    override fun createSingle(data: Map<String, Any?>?): Single<List<VideoEntity>> {
        val query = data?.get(UseCaseParams.PARAM_VIDEO_QUERY) as? String
        val loadMore = data?.get(UseCaseParams.PARAM_LOAD_MORE) as? Boolean
        return if (query != null) {
            if (loadMore != null && loadMore) repository.getMoreVideos(query)
            else repository.getVideos(query)
        } else {
            Single.error { IllegalArgumentException("Query for videos search must be provided.") }
        }
    }

    fun execute(query: String, loadMore: Boolean): Single<List<VideoEntity>> {
        val data = HashMap<String, Any?>().apply {
            put(UseCaseParams.PARAM_VIDEO_QUERY, query)
            put(UseCaseParams.PARAM_LOAD_MORE, loadMore)
        }
        return execute(withData = data)
    }
}