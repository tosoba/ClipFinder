package com.example.there.domain.usecases.videos

import com.example.there.domain.common.Transformer
import com.example.there.domain.entities.videos.VideoEntity
import com.example.there.domain.entities.videos.VideoPlaylistEntity
import com.example.there.domain.repos.videos.IVideosRepository
import com.example.there.domain.usecases.UseCase
import com.example.there.domain.usecases.UseCaseParams
import io.reactivex.Observable

class GetFavouriteVideosFromPlaylist(transformer: Transformer<List<VideoEntity>>,
                                     private val repository: IVideosRepository) : UseCase<List<VideoEntity>>(transformer) {

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

class AddVideoToPlaylist(transformer: Transformer<Unit>,
                         private val repository: IVideosRepository) : UseCase<Unit>(transformer) {

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