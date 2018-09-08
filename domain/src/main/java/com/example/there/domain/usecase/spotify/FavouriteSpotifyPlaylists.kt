package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricFlowableTransformer
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.CompletableUseCase
import com.example.there.domain.usecase.base.FlowableUseCase
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.Flowable

class GetFavouriteSpotifyPlaylists(
        transformer: SymmetricFlowableTransformer<List<PlaylistEntity>>,
        private val repository: ISpotifyRepository
) : FlowableUseCase<List<PlaylistEntity>>(transformer) {

    override fun createFlowable(data: Map<String, Any?>?): Flowable<List<PlaylistEntity>> = repository.getFavouritePlaylists()
}

class InsertSpotifyPlaylist(
        transformer: CompletableTransformer,
        private val repository: ISpotifyRepository
) : CompletableUseCase(transformer) {

    override fun createCompletable(data: Map<String, Any?>?): Completable {
        val playlistEntity = data?.get(UseCaseParams.PARAM_SPOTIFY_PLAYLIST) as? PlaylistEntity
        return if (playlistEntity != null) {
            repository.insertPlaylist(playlistEntity)
        } else {
            Completable.error { IllegalArgumentException("PlaylistEntity must be provided.") }
        }
    }

    fun execute(playlistEntity: PlaylistEntity): Completable {
        val data = HashMap<String, PlaylistEntity>().apply {
            put(UseCaseParams.PARAM_SPOTIFY_PLAYLIST, playlistEntity)
        }
        return execute(withData = data)
    }
}