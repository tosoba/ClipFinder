package com.example.there.domain.usecases.spotify

import com.example.there.domain.common.Transformer
import com.example.there.domain.entities.spotify.PlaylistEntity
import com.example.there.domain.repos.spotify.ISpotifyRepository
import com.example.there.domain.usecases.UseCase
import com.example.there.domain.usecases.UseCaseParams
import io.reactivex.Observable

class GetFavouriteSpotifyPlaylists(transformer: Transformer<List<PlaylistEntity>>,
                            private val repository: ISpotifyRepository) : UseCase<List<PlaylistEntity>>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<List<PlaylistEntity>> = repository.getFavouritePlaylists().toObservable()
}

class InsertSpotifyPlaylist(transformer: Transformer<Unit>,
                     private val repository: ISpotifyRepository) : UseCase<Unit>(transformer) {

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