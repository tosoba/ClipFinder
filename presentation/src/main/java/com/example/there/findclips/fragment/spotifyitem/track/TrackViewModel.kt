package com.example.there.findclips.fragment.spotifyitem.track

import android.graphics.Color
import com.example.there.domain.usecase.spotify.GetAlbum
import com.example.there.domain.usecase.spotify.GetArtists
import com.example.there.domain.usecase.spotify.GetAudioFeatures
import com.example.there.domain.usecase.spotify.GetSimilarTracks
import com.example.there.findclips.base.vm.BaseViewModel
import com.example.there.findclips.model.entity.Track
import com.example.there.findclips.model.mapper.AlbumEntityMapper
import com.example.there.findclips.model.mapper.ArtistEntityMapper
import com.example.there.findclips.model.mapper.TrackEntityMapper
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
        addDisposable(getAlbum.execute(albumId)
                .doFinally { viewState.albumLoadingInProgress.set(false) }
                .subscribe({ viewState.album.set(AlbumEntityMapper.mapFrom(it)) }, ::onError))
    }

    fun loadArtists(artistIds: List<String>) {
        viewState.artistsLoadingInProgress.set(true)
        addDisposable(getArtists.execute(artistIds)
                .doFinally { viewState.artistsLoadingInProgress.set(false) }
                .subscribe({
                    viewState.artists.addAll(it.map(ArtistEntityMapper::mapFrom).sortedBy { it.name })
                }, ::onError))
    }

    fun loadSimilarTracks(track: Track) {
        viewState.similarTracksLoadingInProgress.set(true)
        addDisposable(getSimilarTracks.execute(track.id)
                .doFinally { viewState.similarTracksLoadingInProgress.set(false) }
                .subscribe({
                    viewState.similarTracks.addAll(it.map(TrackEntityMapper::mapFrom).sortedBy { it.name })
                }, ::onError))
    }

    fun loadAudioFeatures(track: Track) {
        addDisposable(getAudioFeatures.execute(TrackEntityMapper.mapBack(track))
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
}