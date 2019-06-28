package com.example.spotifytrack

import android.graphics.Color
import com.example.coreandroid.base.vm.BaseViewModel
import com.example.coreandroid.mapper.spotify.domain
import com.example.coreandroid.mapper.spotify.ui
import com.example.coreandroid.model.spotify.Track
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.usecase.spotify.GetAlbum
import com.example.there.domain.usecase.spotify.GetArtists
import com.example.there.domain.usecase.spotify.GetAudioFeatures
import com.example.there.domain.usecase.spotify.GetSimilarTracks
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import javax.inject.Inject

class TrackViewModel @Inject constructor(
        private val getAlbum: GetAlbum,
        private val getArtists: GetArtists,
        private val getSimilarTracks: GetSimilarTracks,
        private val getAudioFeatures: GetAudioFeatures
) : BaseViewModel() {

    val viewState: TrackViewState = TrackViewState()

    fun loadData(track: Track) {
        loadAlbum(track.albumId)
        loadArtists(artistIds = track.artists.map { it.id })
        loadSimilarTracks(track)
        loadAudioFeatures(track)
    }

    private fun loadAlbum(albumId: String) {
        viewState.albumLoadingInProgress.set(true)
        getAlbum(albumId)
                .doFinally { viewState.albumLoadingInProgress.set(false) }
                .subscribeAndDisposeOnCleared({ viewState.album.set(it.ui) }, ::onError)
    }

    fun loadArtists(artistIds: List<String>) {
        viewState.artistsLoadingInProgress.set(true)
        getArtists(artistIds)
                .doFinally { viewState.artistsLoadingInProgress.set(false) }
                .subscribeAndDisposeOnCleared({ artists ->
                    viewState.artists.addAll(artists.map(ArtistEntity::ui).sortedBy { it.name })
                    viewState.artistsLoadingErrorOccurred.set(false)
                }, getOnErrorWith {
                    viewState.artistsLoadingErrorOccurred.set(true)
                })
    }

    fun loadSimilarTracks(track: Track) {
        viewState.similarTracksLoadingInProgress.set(true)
        getSimilarTracks(track.id)
                .doFinally { viewState.similarTracksLoadingInProgress.set(false) }
                .subscribeAndDisposeOnCleared({ tracks ->
                    viewState.similarTracks.addAll(tracks.map(TrackEntity::ui).sortedBy { it.name })
                    viewState.similarTracksErrorOccurred.set(false)
                }, getOnErrorWith {
                    viewState.similarTracksErrorOccurred.set(true)
                })
    }

    private fun loadAudioFeatures(track: Track) {
        getAudioFeatures(track.domain)
                .subscribeAndDisposeOnCleared({
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
                }, ::onError)
    }
}