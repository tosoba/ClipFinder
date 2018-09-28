package com.example.there.findclips.fragment.spotifyitem.album

import android.util.Log
import com.example.there.data.api.spotify.SpotifyApi
import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.domain.usecase.spotify.*
import com.example.there.findclips.base.vm.BaseSpotifyViewModel
import com.example.there.findclips.model.entity.Album
import com.example.there.findclips.model.mapper.AlbumEntityMapper
import com.example.there.findclips.model.mapper.ArtistEntityMapper
import com.example.there.findclips.model.mapper.TrackEntityMapper
import javax.inject.Inject

class AlbumViewModel @Inject constructor(
        getAccessToken: GetAccessToken,
        private val getArtists: GetArtists,
        private val getTracksFromAlbum: GetTracksFromAlbum,
        private val insertAlbum: InsertAlbum,
        private val deleteAlbum: DeleteAlbum,
        private val isAlbumSaved: IsAlbumSaved
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
        loadAlbumFavouriteState(album)
    }

    fun loadAlbumsArtists(accessToken: AccessTokenEntity, artistIds: List<String>) {
        viewState.artistsLoadingInProgress.set(true)
        addDisposable(getArtists.execute(accessToken, artistIds)
                .doFinally { viewState.artistsLoadingInProgress.set(false) }
                .subscribe({ viewState.artists.addAll(it.map(ArtistEntityMapper::mapFrom)) }, ::onError))
    }

    private var currentOffset = 0
    private var totalItems = 0

    fun loadTracksFromAlbum(accessToken: AccessTokenEntity, albumId: String) {
        if (currentOffset == 0 || (currentOffset < totalItems)) {
            viewState.tracksLoadingInProgress.set(true)
            addDisposable(getTracksFromAlbum.execute(accessToken, albumId, currentOffset)
                    .doFinally { viewState.tracksLoadingInProgress.set(false) }
                    .subscribe({
                        currentOffset = it.offset + SpotifyApi.DEFAULT_LIMIT
                        totalItems = it.totalItems
                        viewState.tracks.addAll(it.items.map(TrackEntityMapper::mapFrom))
                    }, ::onError))
        }
    }

    fun addFavouriteAlbum(
            album: Album
    ) = addDisposable(insertAlbum.execute(AlbumEntityMapper.mapBack(album))
            .subscribe({ viewState.isSavedAsFavourite.set(true) }, { Log.e(javaClass.name, "Insert error.") }))

    fun deleteFavouriteAlbum(
            album: Album
    ) = addDisposable(deleteAlbum.execute(AlbumEntityMapper.mapBack(album))
            .subscribe({ viewState.isSavedAsFavourite.set(false) }, { Log.e(javaClass.name, "Delete error.") }))

    private fun loadAlbumFavouriteState(
            album: Album
    ) = addDisposable(isAlbumSaved.execute(AlbumEntityMapper.mapBack(album))
            .subscribe({ viewState.isSavedAsFavourite.set(it) }, {}))

    override fun onError(t: Throwable) {
        super.onError(t)
        handleErrors(t, onErrorsResolved = { token ->
            lastAlbum?.let { loadAlbumData(token, it) }
        })
    }
}