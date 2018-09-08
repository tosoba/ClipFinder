package com.example.there.domain.usecase.videos

import com.example.there.domain.common.SymmetricFlowableTransformer
import com.example.there.domain.common.SymmetricSingleTransformer
import com.example.there.domain.entity.videos.VideoPlaylistEntity
import com.example.there.domain.repo.videos.IVideosRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.FlowableUseCase
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Flowable
import io.reactivex.Single

class GetFavouriteVideoPlaylists(
        transformer: SymmetricFlowableTransformer<List<VideoPlaylistEntity>>,
        private val repository: IVideosRepository
) : FlowableUseCase<List<VideoPlaylistEntity>>(transformer) {

    override fun createFlowable(data: Map<String, Any?>?): Flowable<List<VideoPlaylistEntity>> = repository.getFavouritePlaylists()
}

class InsertVideoPlaylist(
        transformer: SymmetricSingleTransformer<Long>,
        private val repository: IVideosRepository
) : SingleUseCase<Long>(transformer) {

    override fun createSingle(data: Map<String, Any?>?): Single<Long> {
        val playlistEntity = data?.get(UseCaseParams.PARAM_VIDEO_PLAYLIST) as? VideoPlaylistEntity
        return if (playlistEntity != null) {
            repository.insertPlaylist(playlistEntity)
        } else {
            Single.error { IllegalArgumentException("VideoPlaylistEntity must be provided.") }
        }
    }

    fun execute(playlistEntity: VideoPlaylistEntity): Single<Long> {
        val data = HashMap<String, VideoPlaylistEntity>().apply {
            put(UseCaseParams.PARAM_VIDEO_PLAYLIST, playlistEntity)
        }
        return execute(withData = data)
    }
}