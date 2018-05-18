package com.example.there.findclips.activities.album

import android.arch.lifecycle.MutableLiveData
import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.domain.usecases.spotify.GetAccessToken
import com.example.there.domain.usecases.spotify.GetArtists
import com.example.there.domain.usecases.spotify.GetTracksFromAlbum
import com.example.there.findclips.base.BaseSpotifyViewModel
import com.example.there.findclips.model.entities.Album
import com.example.there.findclips.model.entities.Track
import com.example.there.findclips.model.mappers.ArtistEntityMapper
import com.example.there.findclips.model.mappers.TrackEntityMapper

class AlbumViewModel(getAccessToken: GetAccessToken,
                     private val getArtists: GetArtists,
                     private val getTracksFromAlbum: GetTracksFromAlbum) : BaseSpotifyViewModel(getAccessToken) {

    val viewState: AlbumViewState = AlbumViewState()

    private var lastAlbum: Album? = null

    fun loadAlbumData(accessToken: AccessTokenEntity?, album: Album) {
        lastAlbum = album
        if (accessToken != null && accessToken.isValid) {
            accessTokenLiveData.value = accessToken
            loadData(accessTokenLiveData.value!!, album)
        } else {
            loadAccessToken { loadData(it, album) }
        }
    }

    private fun loadData(accessToken: AccessTokenEntity, album: Album) {
        loadAlbumsArtists(accessToken, artistIds = album.artists.map { it.id })
        loadTracksFromAlbum(accessToken, albumId = album.id)
    }

    private fun loadAlbumsArtists(accessToken: AccessTokenEntity, artistIds: List<String>) {
        viewState.artistsLoadingInProgress.set(true)
        addDisposable(getArtists.execute(accessToken, artistIds)
                .doFinally { viewState.artistsLoadingInProgress.set(false) }
                .subscribe({ viewState.artists.addAll(it.map(ArtistEntityMapper::mapFrom)) }, this::onError))
    }

    private fun loadTracksFromAlbum(accessToken: AccessTokenEntity, albumId: String) {
        viewState.tracksLoadingInProgress.set(true)
        addDisposable(getTracksFromAlbum.execute(accessToken, albumId)
                .doFinally { viewState.tracksLoadingInProgress.set(false) }
                .subscribe({ viewState.tracks.addAll(it.map(TrackEntityMapper::mapFrom).sortedBy { it.trackNumber }) }, this::onError))
    }

    override fun onError(t: Throwable) {
        super.onError(t)
        handleErrors(t, onErrorsResolved = { token ->
            lastAlbum?.let { loadAlbumData(token, it) }
        })
    }
}