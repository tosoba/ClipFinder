package com.example.there.domain.usecase.videos

import com.example.there.domain.common.SymmetricObservableTransformer
import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.entity.videos.VideoPlaylistEntity
import com.example.there.domain.repo.videos.IVideosRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.ObservableUseCase
import io.reactivex.Observable

class GetFavouriteVideosFromPlaylist(transformer: SymmetricObservableTransformer<List<VideoEntity>>,
                                     private val repository: IVideosRepository) : ObservableUseCase<List<VideoEntity>>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<List<VideoEntity>> {
        val playlistEntity = data?.get(UseCaseParams.PARAM_VIDEO_PLAYLIST) as? VideoPlaylistEntity
        return if (playlistEntity != null ) {
            repository.getVideosFromPlaylist(playlistEntity.id).toObservable()
        } else {
            Observable.error { IllegalArgumentException("PlaylistEntity must be provided.") }
        }
    }

    fun execute(playlistEntity: VideoPlaylistEntity): Observable<List<VideoEntity>> {
        val data = HashMap<String, Any?>().apply {
            put(UseCaseParams.PARAM_VIDEO_PLAYLIST, playlistEntity)
        }
        return execute(withData = data)
    }
}

class AddVideoToPlaylist(transformer: SymmetricObservableTransformer<Unit>,
                         private val repository: IVideosRepository) : ObservableUseCase<Unit>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<Unit> {
        val playlistEntity = data?.get(UseCaseParams.PARAM_VIDEO_PLAYLIST) as? VideoPlaylistEntity
        val videoEntity = data?.get(UseCaseParams.PARAM_VIDEO) as? VideoEntity
        return if (playlistEntity != null && videoEntity != null) {
            repository.addVideoToPlaylist(videoEntity, playlistEntity).toObservable()
        } else {
            Observable.error { IllegalArgumentException("VideoEntity and PlaylistEntity must be provided.") }
        }
    }

    fun execute(playlistEntity: VideoPlaylistEntity, videoEntity: VideoEntity): Observable<Unit> {
        val data = HashMap<String, Any?>().apply {
            put(UseCaseParams.PARAM_VIDEO_PLAYLIST, playlistEntity)
            put(UseCaseParams.PARAM_VIDEO, videoEntity)
        }
        return execute(withData = data)
    }
}