package com.example.there.findclips.spotify.spotifyitem.artist

import android.databinding.ObservableField
import android.util.Log
import com.example.there.domain.usecase.spotify.*
import com.example.there.findclips.base.vm.BaseViewModel
import com.example.there.findclips.model.entity.spotify.Artist
import com.example.there.findclips.model.mapper.AlbumEntityMapper
import com.example.there.findclips.model.mapper.ArtistEntityMapper
import com.example.there.findclips.model.mapper.TrackEntityMapper
import java.util.*
import javax.inject.Inject

class ArtistViewModel @Inject constructor(
        private val getAlbumsFromArtist: GetAlbumsFromArtist,
        private val getTopTracksFromArtist: GetTopTracksFromArtist,
        private val getRelatedArtists: GetRelatedArtists,
        private val insertArtist: InsertArtist,
        private val deleteArtist: DeleteArtist,
        private val isArtistSaved: IsArtistSaved
) : BaseViewModel() {

    private val viewStates: Stack<ArtistViewState> = Stack()

    val viewState: ArtistViewState = ArtistViewState()

    val lastArtist: Artist?
        get() = viewState.artist.get()

    fun onBackPressed(): Boolean = if (viewStates.size < 2) false
    else {
        viewStates.pop()
        val previous = viewStates.peek()
        viewState.clearAll()
        viewState.artist.set(previous.artist.get())
        viewState.albums.addAll(previous.albums)
        viewState.topTracks.addAll(previous.topTracks)
        viewState.relatedArtists.addAll(previous.relatedArtists)
        viewState.isSavedAsFavourite.set(previous.isSavedAsFavourite.get())
        true
    }

    fun loadArtistData(artist: Artist) {
        if (artist.id == lastArtist?.id) return

        viewState.artist.set(artist)
        viewStates.push(ArtistViewState(ObservableField(artist)))

        loadData(artist = artist)
    }

    private fun loadData(artist: Artist) {
        viewState.clearAll()
        loadAlbumsFromArtist(artist.id)
        loadTopTracksFromArtist(artist.id)
        loadRelatedArtists(artist.id)
        loadArtistFavouriteState()
    }

    fun loadAlbumsFromArtist(artistId: String) {
        viewState.albumsLoadingInProgress.set(true)
        getAlbumsFromArtist.execute(artistId)
                .doFinally { viewState.albumsLoadingInProgress.set(false) }
                .subscribeAndDisposeOnCleared({
                    val toAdd = it.map(AlbumEntityMapper::mapFrom)
                    viewStates.peek().albums.addAll(toAdd)
                    viewState.albums.addAll(toAdd)
                    viewState.albumsLoadingErrorOccurred.set(false)
                }, getOnErrorWith {
                    viewState.albumsLoadingErrorOccurred.set(true)
                })
    }

    fun loadTopTracksFromArtist(artistId: String) {
        viewState.topTracksLoadingInProgress.set(true)
        getTopTracksFromArtist.execute(artistId)
                .doFinally { viewState.topTracksLoadingInProgress.set(false) }
                .subscribeAndDisposeOnCleared({
                    val toAdd = it.map(TrackEntityMapper::mapFrom)
                    viewStates.peek().topTracks.addAll(toAdd)
                    viewState.topTracks.addAll(toAdd)
                    viewState.topTracksLoadingErrorOccurred.set(false)
                }, getOnErrorWith {
                    viewState.topTracksLoadingErrorOccurred.set(true)
                })
    }

    fun loadRelatedArtists(artistId: String) {
        viewState.relatedArtistsLoadingInProgress.set(true)
        getRelatedArtists.execute(artistId)
                .doFinally { viewState.relatedArtistsLoadingInProgress.set(false) }
                .subscribeAndDisposeOnCleared({
                    val toAdd = it.map(ArtistEntityMapper::mapFrom)
                    viewStates.peek().relatedArtists.addAll(toAdd)
                    viewState.relatedArtists.addAll(toAdd)
                    viewState.relatedArtistsLoadingErrorOccurred.set(false)
                }, getOnErrorWith {
                    viewState.relatedArtistsLoadingErrorOccurred.set(true)
                })
    }

    fun addFavouriteArtist() = lastArtist?.let { artist ->
        insertArtist.execute(ArtistEntityMapper.mapBack(artist))
                .subscribeAndDisposeOnCleared({
                    viewStates.peek().isSavedAsFavourite.set(true)
                    viewState.isSavedAsFavourite.set(true)
                }, { Log.e(javaClass.name, "Insert error.") })
    }

    fun deleteFavouriteArtist() = lastArtist?.let { artist ->
        deleteArtist.execute(ArtistEntityMapper.mapBack(artist))
                .subscribeAndDisposeOnCleared({
                    viewStates.peek().isSavedAsFavourite.set(false)
                    viewState.isSavedAsFavourite.set(false)
                }, { Log.e(javaClass.name, "Delete error.") })
    }

    private fun loadArtistFavouriteState() = lastArtist?.let { artist ->
        isArtistSaved.execute(ArtistEntityMapper.mapBack(artist))
                .subscribeAndDisposeOnCleared {
                    viewStates.peek().isSavedAsFavourite.set(it)
                    viewState.isSavedAsFavourite.set(it)
                }
    }
}