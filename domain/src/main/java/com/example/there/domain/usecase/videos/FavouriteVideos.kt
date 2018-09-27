package com.example.there.domain.usecase.videos

import com.example.there.domain.common.SymmetricFlowableTransformer
import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.entity.videos.VideoPlaylistEntity
import com.example.there.domain.repo.videos.IVideosRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.CompletableUseCase
import com.example.there.domain.usecase.base.FlowableUseCase
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.Flowable

class GetFavouriteVideosFromPlaylist(
        transformer: SymmetricFlowableTransformer<List<VideoEntity>>,
        private val repository: IVideosRepository
) : FlowableUseCase<List<VideoEntity>>(transformer) {

    override fun createFlowable(data: Map<String, Any?>?): Flowable<List<VideoEntity>> {
        val playlistEntity = data?.get(UseCaseParams.PARAM_VIDEO_PLAYLIST) as? VideoPlaylistEntity
        return if (playlistEntity?.id != null) {
            repository.getVideosFromPlaylist(playlistEntity.id)
        } else {
            Flowable.error { IllegalArgumentException("PlaylistEntity must be provided.") }
        }
    }

    fun execute(playlistEntity: VideoPlaylistEntity): Flowable<List<VideoEntity>> {
        val data = HashMap<String, Any?>().apply {
            put(UseCaseParams.PARAM_VIDEO_PLAYLIST, playlistEntity)
        }
        return execute(withData = data)
    }
}

class AddVideoToPlaylist(
        transformer: CompletableTransformer,
        private val repository: IVideosRepository
) : CompletableUseCase(transformer) {

    override fun createCompletable(data: Map<String, Any?>?): Completable {
        val playlistEntity = data?.get(UseCaseParams.PARAM_VIDEO_PLAYLIST) as? VideoPlaylistEntity
        val videoEntity = data?.get(UseCaseParams.PARAM_VIDEO) as? VideoEntity
        return if (playlistEntity != null && videoEntity != null) {
            repository.addVideoToPlaylist(videoEntity, playlistEntity)
        } else {
            Completable.error { IllegalArgumentException("VideoEntity and PlaylistEntity must be provided.") }
        }
    }

    fun execute(playlistEntity: VideoPlaylistEntity, videoEntity: VideoEntity): Completable {
        val data = HashMap<String, Any?>().apply {
            put(UseCaseParams.PARAM_VIDEO_PLAYLIST, playlistEntity)
            put(UseCaseParams.PARAM_VIDEO, videoEntity)
        }
        return execute(withData = data)
    }
}

class DeleteVideo(
        transformer: CompletableTransformer,
        private val repository: IVideosRepository
) : CompletableUseCase(transformer) {
    override fun createCompletable(data: Map<String, Any?>?): Completable {
        val videoEntity = data?.get(UseCaseParams.PARAM_VIDEO) as? VideoEntity
        return if (videoEntity != null) {
            repository.deleteVideo(videoEntity)
        } else {
            Completable.error { IllegalArgumentException("VideoEntity must be provided.") }
        }
    }

    fun execute(videoEntity: VideoEntity): Completable {
        val data = HashMap<String, Any?>().apply {
            put(UseCaseParams.PARAM_VIDEO, videoEntity)
        }
        return execute(withData = data)
    }
}