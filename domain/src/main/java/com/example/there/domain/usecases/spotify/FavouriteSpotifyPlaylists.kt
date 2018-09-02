package com.example.there.domain.usecases.spotify

import com.example.there.domain.common.SymmetricObservableTransformer
import com.example.there.domain.entities.spotify.PlaylistEntity
import com.example.there.domain.repos.spotify.ISpotifyRepository
import com.example.there.domain.usecases.UseCaseParams
import com.example.there.domain.usecases.base.ObservableUseCase
import io.reactivex.Observable

class GetFavouriteSpotifyPlaylists(transformer: SymmetricObservableTransformer<List<PlaylistEntity>>,
                                   private val repository: ISpotifyRepository) : ObservableUseCase<List<PlaylistEntity>>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<List<PlaylistEntity>> = repository.getFavouritePlaylists().toObservable()
}

class InsertSpotifyPlaylist(transformer: SymmetricObservableTransformer<Unit>,
                            private val repository: ISpotifyRepository) : ObservableUseCase<Unit>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<Unit> {
        val playlistEntity = data?.get(UseCaseParams.PARAM_SPOTIFY_PLAYLIST) as? PlaylistEntity
        return if (playlistEntity != null) {
            repository.insertPlaylist(playlistEntity).toObservable()
        } else {
            Observable.error { IllegalArgumentException("PlaylistEntity must be provided.") }
        }
    }

    fun execute(playlistEntity: PlaylistEntity): Observable<Unit> {
        val data = HashMap<String, PlaylistEntity>().apply {
            put(UseCaseParams.PARAM_SPOTIFY_PLAYLIST, playlistEntity)
        }
        return execute(withData = data)
    }
}