package com.example.there.findclips.fragment.spotifyitem.track

import android.graphics.Color
import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.domain.usecase.spotify.*
import com.example.there.findclips.base.vm.BaseSpotifyViewModel
import com.example.there.findclips.model.entity.Track
import com.example.there.findclips.model.mapper.AlbumEntityMapper
import com.example.there.findclips.model.mapper.ArtistEntityMapper
import com.example.there.findclips.model.mapper.TrackEntityMapper
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import javax.inject.Inject

class TrackViewModel @Inject constructor(
        getAccessToken: GetAccessToken,
        private val getAlbum: GetAlbum,
        private val getArtists: GetArtists,
        private val getSimilarTracks: GetSimilarTracks,
        private val getAudioFeatures: GetAudioFeatures
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
        loadAudioFeatures(accessToken, track)
    }

    private fun loadAlbum(accessToken: AccessTokenEntity, albumId: String) {
        viewState.albumLoadingInProgress.set(true)
        addDisposable(getAlbum.execute(accessToken, albumId)
                .doFinally { viewState.albumLoadingInProgress.set(false) }
                .subscribe({ viewState.album.set(AlbumEntityMapper.mapFrom(it)) }, ::onError))
    }

    fun loadArtists(accessToken: AccessTokenEntity, artistIds: List<String>) {
        viewState.artistsLoadingInProgress.set(true)
        addDisposable(getArtists.execute(accessToken, artistIds)
                .doFinally { viewState.artistsLoadingInProgress.set(false) }
                .subscribe({
                    viewState.artists.addAll(it.map(ArtistEntityMapper::mapFrom).sortedBy { it.name })
                }, ::onError))
    }

    fun loadSimilarTracks(accessToken: AccessTokenEntity, track: Track) {
        viewState.similarTracksLoadingInProgress.set(true)
        addDisposable(getSimilarTracks.execute(accessToken, track.id)
                .doFinally { viewState.similarTracksLoadingInProgress.set(false) }
                .subscribe({
                    viewState.similarTracks.addAll(it.map(TrackEntityMapper::mapFrom).sortedBy { it.name })
                }, ::onError))
    }

    fun loadAudioFeatures(accessToken: AccessTokenEntity, track: Track) {
        addDisposable(getAudioFeatures.execute(accessToken, TrackEntityMapper.mapBack(track))
                .subscribe({
                    val entries = ArrayList<RadarEntry>().apply {
                        add(RadarEntry(it.acousticness))
                        add(RadarEntry(it.danceability))
                        add(RadarEntry(it.energy))
                        add(RadarEntry(it.instrumentalness))
                        add(RadarEntry(it.liveness))
                        add(RadarEntry(it.speechiness))
                        add(RadarEntry(it.valence))
                    }
                    viewState.audioFeaturesChartData.set(RadarData(
                            RadarDataSet(entries, "Audio features")
                    ).apply {
                        setValueTextSize(12f)
                        setDrawValues(false)
                        setValueTextColor(Color.WHITE)
                    })
                }, ::onError))
    }

    override fun onError(t: Throwable) {
        super.onError(t)
        handleErrors(t, onErrorsResolved = { token ->
            lastTrack?.let { loadDataForTrack(token, it) }
        })
    }
}