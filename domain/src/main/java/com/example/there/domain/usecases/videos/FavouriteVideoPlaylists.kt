package com.example.there.domain.usecases.videos

import com.example.there.domain.common.SymmetricObservableTransformer
import com.example.there.domain.entities.videos.VideoPlaylistEntity
import com.example.there.domain.repos.videos.IVideosRepository
import com.example.there.domain.usecases.UseCaseParams
import com.example.there.domain.usecases.base.ObservableUseCase
import io.reactivex.Observable

class GetFavouriteVideoPlaylists(transformer: SymmetricObservableTransformer<List<VideoPlaylistEntity>>,
                                 private val repository: IVideosRepository) : ObservableUseCase<List<VideoPlaylistEntity>>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<List<VideoPlaylistEntity>> = repository.getFavouritePlaylists().toObservable()
}

class InsertVideoPlaylist(transformer: SymmetricObservableTransformer<Long>,
                          private val repository: IVideosRepository) : ObservableUseCase<Long>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<Long> {
        val playlistEntity = data?.get(UseCaseParams.PARAM_VIDEO_PLAYLIST) as? VideoPlaylistEntity
        return if (playlistEntity != null) {
            repository.insertPlaylist(playlistEntity).toObservable()
        } else {
            Observable.error { IllegalArgumentException("VideoPlaylistEntity must be provided.") }
        }
    }

    fun execute(playlistEntity: VideoPlaylistEntity): Observable<Long> {
        val data = HashMap<String, VideoPlaylistEntity>().apply {
            put(UseCaseParams.PARAM_VIDEO_PLAYLIST, playlistEntity)
        }
        return execute(withData = data)
    }
}