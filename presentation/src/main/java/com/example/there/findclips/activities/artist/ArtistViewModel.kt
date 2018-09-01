package com.example.there.findclips.activities.artist

import android.databinding.ObservableField
import android.util.Log
import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.domain.usecases.spotify.*
import com.example.there.findclips.base.vm.BaseSpotifyViewModel
import com.example.there.findclips.model.entities.Artist
import com.example.there.findclips.model.mappers.AlbumEntityMapper
import com.example.there.findclips.model.mappers.ArtistEntityMapper
import com.example.there.findclips.model.mappers.TrackEntityMapper
import java.util.*
import javax.inject.Inject

class ArtistViewModel @Inject constructor(
        getAccessToken: GetAccessToken,
        private val getAlbumsFromArtist: GetAlbumsFromArtist,
        private val getTopTracksFromArtist: GetTopTracksFromArtist,
        private val getRelatedArtists: GetRelatedArtists,
        private val insertArtist: InsertArtist
) : BaseSpotifyViewModel(getAccessToken) {

    private val viewStates: Stack<ArtistViewState> = Stack()

    val viewState: ArtistViewState = ArtistViewState()

    private val lastArtist: Artist?
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
        true
    }

    fun loadArtistData(accessToken: AccessTokenEntity?, artist: Artist) {
        if (artist.id == lastArtist?.id) return

        viewState.artist.set(artist)
        viewStates.push(ArtistViewState(ObservableField(artist)))

        if (accessToken != null && accessToken.isValid) {
            accessTokenLiveData.value = accessToken
            loadData(accessTokenLiveData.value!!, artist = artist)
        } else {
            loadAccessToken { loadData(it, artist) }
        }
    }

    private fun loadData(accessToken: AccessTokenEntity, artist: Artist) {
        viewState.clearAll()
        loadAlbumsFromArtist(accessToken, artist.id)
        loadTopTracksFromArtist(accessToken, artist.id)
        loadRelatedArtists(accessToken, artist.id)
    }

    private fun loadAlbumsFromArtist(accessToken: AccessTokenEntity, artistId: String) {
        viewState.albumsLoadingInProgress.set(true)
        addDisposable(getAlbumsFromArtist.execute(accessToken, artistId)
                .doFinally { viewState.albumsLoadingInProgress.set(false) }
                .subscribe({
                    val toAdd = it.map(AlbumEntityMapper::mapFrom)
                    viewStates.peek().albums.addAll(toAdd)
                    viewState.albums.addAll(toAdd)
                }, this::onError))
    }

    private fun loadTopTracksFromArtist(accessToken: AccessTokenEntity, artistId: String) {
        viewState.topTracksLoadingInProgress.set(true)
        addDisposable(getTopTracksFromArtist.execute(accessToken, artistId)
                .doFinally { viewState.topTracksLoadingInProgress.set(false) }
                .subscribe({
                    val toAdd = it.map(TrackEntityMapper::mapFrom)
                    viewStates.peek().topTracks.addAll(toAdd)
                    viewState.topTracks.addAll(toAdd)
                }, this::onError))
    }

    private fun loadRelatedArtists(accessToken: AccessTokenEntity, artistId: String) {
        viewState.relatedArtistsLoadingInProgress.set(true)
        addDisposable(getRelatedArtists.execute(accessToken, artistId)
                .doFinally { viewState.relatedArtistsLoadingInProgress.set(false) }
                .subscribe({
                    val toAdd = it.map(ArtistEntityMapper::mapFrom)
                    viewStates.peek().relatedArtists.addAll(toAdd)
                    viewState.relatedArtists.addAll(toAdd)
                }, this::onError))
    }

    fun addFavouriteArtist() {
        lastArtist?.let { artist ->
            addDisposable(insertArtist.execute(ArtistEntityMapper.mapBack(artist))
                    .subscribe({}, { Log.e(javaClass.name, "Insert error.") }))
        }
    }

    override fun onError(t: Throwable) {
        super.onError(t)
        handleErrors(t, onErrorsResolved = { token ->
            lastArtist?.let { loadArtistData(token, it) }
        })
    }
}