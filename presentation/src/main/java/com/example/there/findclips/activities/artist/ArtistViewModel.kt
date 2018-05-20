package com.example.there.findclips.activities.artist

import android.util.Log
import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.domain.usecases.spotify.*
import com.example.there.findclips.base.BaseSpotifyViewModel
import com.example.there.findclips.model.entities.Artist
import com.example.there.findclips.model.mappers.AlbumEntityMapper
import com.example.there.findclips.model.mappers.ArtistEntityMapper
import com.example.there.findclips.model.mappers.TrackEntityMapper

class ArtistViewModel(getAccessToken: GetAccessToken,
                      private val getAlbumsFromArtist: GetAlbumsFromArtist,
                      private val getTopTracksFromArtist: GetTopTracksFromArtist,
                      private val getRelatedArtists: GetRelatedArtists,
                      private val insertArtist: InsertArtist): BaseSpotifyViewModel(getAccessToken) {

    val viewState: ArtistViewState = ArtistViewState()

    private var lastArtist: Artist? = null

    fun loadArtistData(accessToken: AccessTokenEntity?, artist: Artist) {
        lastArtist = artist
        if (accessToken != null && accessToken.isValid) {
            accessTokenLiveData.value = accessToken
            loadData(accessTokenLiveData.value!!, artist = artist)
        } else {
            loadAccessToken { loadData(it, artist) }
        }
    }

    private fun loadData(accessToken: AccessTokenEntity, artist: Artist) {
        loadAlbumsFromArtist(accessToken, artist.id)
        loadTopTracksFromArtist(accessToken, artist.id)
        loadRelatedArtists(accessToken, artist.id)
    }

    private fun loadAlbumsFromArtist(accessToken: AccessTokenEntity,artistId: String) {
        viewState.albumsLoadingInProgress.set(true)
        addDisposable(getAlbumsFromArtist.execute(accessToken, artistId)
                .doFinally { viewState.albumsLoadingInProgress.set(false) }
                .subscribe({ viewState.albums.addAll(it.map(AlbumEntityMapper::mapFrom)) }, this::onError))
    }

    private fun loadTopTracksFromArtist(accessToken: AccessTokenEntity,artistId: String) {
        viewState.topTracksLoadingInProgress.set(true)
        addDisposable(getTopTracksFromArtist.execute(accessToken, artistId)
                .doFinally { viewState.topTracksLoadingInProgress.set(false) }
                .subscribe({ viewState.topTracks.addAll(it.map(TrackEntityMapper::mapFrom)) }, this::onError))
    }

    private fun loadRelatedArtists(accessToken: AccessTokenEntity,artistId: String) {
        viewState.relatedArtistsLoadingInProgress.set(true)
        addDisposable(getRelatedArtists.execute(accessToken, artistId)
                .doFinally { viewState.relatedArtistsLoadingInProgress.set(false) }
                .subscribe({ viewState.relatedArtists.addAll(it.map(ArtistEntityMapper::mapFrom)) }, this::onError))
    }

    fun addFavouriteAlbum(artist: Artist) {
        addDisposable(insertArtist.execute(ArtistEntityMapper.mapBack(artist)).subscribe({}, { Log.e(javaClass.name, "Insert error.") }))
    }

    override fun onError(t: Throwable) {
        super.onError(t)
        handleErrors(t, onErrorsResolved = { token ->
            lastArtist?.let { loadArtistData(token, it) }
        })
    }
}