package com.example.there.findclips.spotify.spotifyitem.album

import android.util.Log
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.usecase.spotify.*
import com.example.there.findclips.base.vm.BaseViewModel
import com.example.there.findclips.model.entity.spotify.Album
import com.example.there.findclips.model.mapper.domain
import com.example.there.findclips.model.mapper.ui
import javax.inject.Inject

class AlbumViewModel @Inject constructor(
        private val getArtists: GetArtists,
        private val getTracksFromAlbum: GetTracksFromAlbum,
        private val insertAlbum: InsertAlbum,
        private val deleteAlbum: DeleteAlbum,
        private val isAlbumSaved: IsAlbumSaved
) : BaseViewModel() {

    val viewState: AlbumViewState = AlbumViewState()

    private var lastAlbum: Album? = null

    fun loadAlbumData(album: Album) {
        lastAlbum = album
        loadData(album)
    }

    private fun loadData(album: Album) {
        loadAlbumsArtists(artistIds = album.artists.map { it.id })
        loadTracksFromAlbum(albumId = album.id)
        loadAlbumFavouriteState(album)
    }

    fun loadAlbumsArtists(artistIds: List<String>) {
        viewState.artistsLoadingInProgress.set(true)
        getArtists.execute(artistIds)
                .doFinally { viewState.artistsLoadingInProgress.set(false) }
                .subscribeAndDisposeOnCleared({
                    viewState.artists.addAll(it.map(ArtistEntity::ui))
                    viewState.artistsLoadingErrorOccurred.set(false)
                }, getOnErrorWith {
                    viewState.artistsLoadingErrorOccurred.set(true)
                })
    }

    fun loadTracksFromAlbum(albumId: String) {
        viewState.tracksLoadingInProgress.set(true)
        getTracksFromAlbum.execute(albumId)
                .doFinally { viewState.tracksLoadingInProgress.set(false) }
                .subscribeAndDisposeOnCleared({ viewState.tracks.addAll(it.items.map(TrackEntity::ui)) }, ::onError)
    }

    fun addFavouriteAlbum(
            album: Album
    ) = insertAlbum.execute(album.domain)
            .subscribeAndDisposeOnCleared({ viewState.isSavedAsFavourite.set(true) }, { Log.e(javaClass.name, "Insert error.") })

    fun deleteFavouriteAlbum(
            album: Album
    ) = deleteAlbum.execute(album.domain)
            .subscribeAndDisposeOnCleared({ viewState.isSavedAsFavourite.set(false) }, { Log.e(javaClass.name, "Delete error.") })

    private fun loadAlbumFavouriteState(
            album: Album
    ) = isAlbumSaved.execute(album.domain)
            .subscribeAndDisposeOnCleared { viewState.isSavedAsFavourite.set(it) }
}