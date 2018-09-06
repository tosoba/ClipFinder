package com.example.there.findclips.activities.album

import android.util.Log
import com.example.there.data.apis.spotify.SpotifyApi
import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.domain.usecases.spotify.GetAccessToken
import com.example.there.domain.usecases.spotify.GetArtists
import com.example.there.domain.usecases.spotify.GetTracksFromAlbum
import com.example.there.domain.usecases.spotify.InsertAlbum
import com.example.there.findclips.base.vm.BaseSpotifyViewModel
import com.example.there.findclips.model.entities.Album
import com.example.there.findclips.model.mappers.AlbumEntityMapper
import com.example.there.findclips.model.mappers.ArtistEntityMapper
import com.example.there.findclips.model.mappers.TrackEntityMapper
import javax.inject.Inject

class AlbumViewModel @Inject constructor(
        getAccessToken: GetAccessToken,
        private val getArtists: GetArtists,
        private val getTracksFromAlbum: GetTracksFromAlbum,
        private val insertAlbum: InsertAlbum
) : BaseSpotifyViewModel(getAccessToken) {

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

    private var currentOffset = 0
    private var totalItems = 0

    fun loadTracksFromAlbum(accessToken: AccessTokenEntity, albumId: String) {
        if (currentOffset == 0 || (currentOffset < totalItems)) {
            viewState.tracksLoadingInProgress.set(true)
            addDisposable(getTracksFromAlbum.execute(accessToken, albumId, currentOffset)
                    .doFinally { viewState.tracksLoadingInProgress.set(false) }
                    .subscribe({
                        currentOffset = it.offset + SpotifyApi.DEFAULT_LIMIT.toInt()
                        totalItems = it.totalItems
                        viewState.tracks.addAll(it.tracks.map(TrackEntityMapper::mapFrom))
                    }, ::onError))
        }
    }

    fun addFavouriteAlbum(album: Album) {
        addDisposable(insertAlbum.execute(AlbumEntityMapper.mapBack(album)).subscribe({}, { Log.e(javaClass.name, "Insert error.") }))
    }

    override fun onError(t: Throwable) {
        super.onError(t)
        handleErrors(t, onErrorsResolved = { token ->
            lastAlbum?.let { loadAlbumData(token, it) }
        })
    }
}