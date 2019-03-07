package com.example.there.findclips.spotify.spotifyitem.artist

import android.databinding.ObservableField
import android.util.Log
import com.example.there.domain.usecase.spotify.*
import com.example.there.findclips.base.vm.BaseViewModel
import com.example.there.findclips.model.entity.Artist
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
        addDisposable(getAlbumsFromArtist.execute(artistId)
                .doFinally { viewState.albumsLoadingInProgress.set(false) }
                .subscribe({
                    val toAdd = it.map(AlbumEntityMapper::mapFrom)
                    viewStates.peek().albums.addAll(toAdd)
                    viewState.albums.addAll(toAdd)
                }, ::onError))
    }

    fun loadTopTracksFromArtist(artistId: String) {
        viewState.topTracksLoadingInProgress.set(true)
        addDisposable(getTopTracksFromArtist.execute(artistId)
                .doFinally { viewState.topTracksLoadingInProgress.set(false) }
                .subscribe({
                    val toAdd = it.map(TrackEntityMapper::mapFrom)
                    viewStates.peek().topTracks.addAll(toAdd)
                    viewState.topTracks.addAll(toAdd)
                }, ::onError))
    }

    fun loadRelatedArtists(artistId: String) {
        viewState.relatedArtistsLoadingInProgress.set(true)
        addDisposable(getRelatedArtists.execute(artistId)
                .doFinally { viewState.relatedArtistsLoadingInProgress.set(false) }
                .subscribe({
                    val toAdd = it.map(ArtistEntityMapper::mapFrom)
                    viewStates.peek().relatedArtists.addAll(toAdd)
                    viewState.relatedArtists.addAll(toAdd)
                }, ::onError))
    }

    fun addFavouriteArtist() = lastArtist?.let { artist ->
        addDisposable(insertArtist.execute(ArtistEntityMapper.mapBack(artist))
                .subscribe({
                    viewStates.peek().isSavedAsFavourite.set(true)
                    viewState.isSavedAsFavourite.set(true)
                }, { Log.e(javaClass.name, "Insert error.") }))
    }

    fun deleteFavouriteArtist() = lastArtist?.let { artist ->
        addDisposable(deleteArtist.execute(ArtistEntityMapper.mapBack(artist))
                .subscribe({
                    viewStates.peek().isSavedAsFavourite.set(false)
                    viewState.isSavedAsFavourite.set(false)
                }, { Log.e(javaClass.name, "Delete error.") }))
    }

    private fun loadArtistFavouriteState() = lastArtist?.let { artist ->
        addDisposable(isArtistSaved.execute(ArtistEntityMapper.mapBack(artist))
                .subscribe({
                    viewStates.peek().isSavedAsFavourite.set(it)
                    viewState.isSavedAsFavourite.set(it)
                }, {}))
    }
}