package com.example.there.findclips.fragment.spotifyitem.track

import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.domain.usecase.spotify.GetAccessToken
import com.example.there.domain.usecase.spotify.GetAlbum
import com.example.there.domain.usecase.spotify.GetArtists
import com.example.there.domain.usecase.spotify.GetSimilarTracks
import com.example.there.findclips.base.vm.BaseSpotifyViewModel
import com.example.there.findclips.model.entity.Track
import com.example.there.findclips.model.mapper.AlbumEntityMapper
import com.example.there.findclips.model.mapper.ArtistEntityMapper
import com.example.there.findclips.model.mapper.TrackEntityMapper
import javax.inject.Inject

class TrackViewModel @Inject constructor(
        getAccessToken: GetAccessToken,
        private val getAlbum: GetAlbum,
        private val getArtists: GetArtists,
        private val getSimilarTracks: GetSimilarTracks
) : BaseSpotifyViewModel(getAccessToken) {

    val viewState: TrackViewState = TrackViewState()

    private var lastTrack: Track? = null

    fun loadDataForTrack(accessToken: AccessTokenEntity?, track: Track) {
        lastTrack = track
        if (accessToken != null && accessToken.isValid) {
            accessTokenLiveData.value = accessToken
            loadData(accessTokenLiveData.value!!, track)
        } else {
            loadAccessToken { loadData(it, track) }
        }
    }

    private fun loadData(accessToken: AccessTokenEntity, track: Track) {
        loadAlbum(accessToken, track.albumId)
        loadArtists(accessToken, artistIds = track.artists.map { it.id })
        loadSimilarTracks(accessToken, track)
    }

    private fun loadAlbum(accessToken: AccessTokenEntity, albumId: String) {
        viewState.albumLoadingInProgress.set(true)
        addDisposable(getAlbum.execute(accessToken, albumId)
                .doFinally { viewState.albumLoadingInProgress.set(false) }
                .subscribe({ viewState.album.set(AlbumEntityMapper.mapFrom(it)) }, this::onError))
    }

    fun loadArtists(accessToken: AccessTokenEntity, artistIds: List<String>) {
        viewState.artistsLoadingInProgress.set(true)
        addDisposable(getArtists.execute(accessToken, artistIds)
                .doFinally { viewState.artistsLoadingInProgress.set(false) }
                .subscribe({
                    viewState.artists.addAll(it.map(ArtistEntityMapper::mapFrom).sortedBy { it.name })
                }, this::onError))
    }

    fun loadSimilarTracks(accessToken: AccessTokenEntity, track: Track) {
        viewState.similarTracksLoadingInProgress.set(true)
        addDisposable(getSimilarTracks.execute(accessToken, track.id)
                .doFinally { viewState.similarTracksLoadingInProgress.set(false) }
                .subscribe({
                    viewState.similarTracks.addAll(it.map(TrackEntityMapper::mapFrom).sortedBy { it.name })
                }, this::onError))
    }

    override fun onError(t: Throwable) {
        super.onError(t)
        handleErrors(t, onErrorsResolved = { token ->
            lastTrack?.let { loadDataForTrack(token, it) }
        })
    }
}