package com.clipfinder.spotify.track

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.clipfinder.core.spotify.usecase.GetAlbum
import com.clipfinder.core.spotify.usecase.GetArtists
import com.clipfinder.core.spotify.usecase.GetAudioFeatures
import com.clipfinder.core.spotify.usecase.GetSimilarTracks
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.model.retryLoadItemsOnNetworkAvailable
import com.example.core.android.model.retryLoadOnNetworkAvailable
import com.example.core.android.spotify.model.Album
import com.example.core.android.spotify.model.Artist
import com.example.core.android.spotify.model.Track
import com.example.core.model.map
import com.example.core.model.mapData
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import org.koin.android.ext.android.get

class TrackViewModel(
    initialState: TrackViewState,
    private val getAlbum: GetAlbum,
    private val getArtists: GetArtists,
    private val getSimilarTracks: GetSimilarTracks,
    private val getAudioFeatures: GetAudioFeatures,
    context: Context
) : MvRxViewModel<TrackViewState>(initialState) {

    init {
        loadData()
        handleConnectivityChanges(context)
    }

    fun onNewTrack(track: Track) {
        setState { TrackViewState(track = track) }
        loadData()
    }

    private fun loadData() {
        loadAlbum()
        loadArtists()
        loadSimilarTracks()
        loadAudioFeatures()
    }

    fun loadAlbum() = loadNullable(TrackViewState::album, getAlbum::withState) { copy(album = it) }

    fun loadArtists() = load(TrackViewState::artists, getArtists::withState) { copy(artists = it) }

    fun loadSimilarTracks() =
        load(TrackViewState::similarTracks, getSimilarTracks::withState) { copy(similarTracks = it) }

    fun loadAudioFeatures() =
        loadNullable(TrackViewState::audioFeaturesChartData, getAudioFeatures::withState) { copy(audioFeaturesChartData = it) }

    @SuppressLint("MissingPermission")
    private fun handleConnectivityChanges(context: Context) {
        context.handleConnectivityChanges { (_, album, artists, tracks, audioFeatures) ->
            if (album.retryLoadOnNetworkAvailable) loadAlbum()
            if (artists.retryLoadItemsOnNetworkAvailable) loadArtists()
            if (tracks.retryLoadItemsOnNetworkAvailable) loadSimilarTracks()
            if (audioFeatures.retryLoadOnNetworkAvailable) loadAudioFeatures()
        }
    }

    companion object : MvRxViewModelFactory<TrackViewModel, TrackViewState> {
        override fun create(viewModelContext: ViewModelContext, state: TrackViewState): TrackViewModel? = TrackViewModel(
            state,
            viewModelContext.activity.get(),
            viewModelContext.activity.get(),
            viewModelContext.activity.get(),
            viewModelContext.activity.get(),
            viewModelContext.app()
        )
    }
}

private fun GetAlbum.withState(state: TrackViewState) = this(applySchedulers = false, args = state.track.album.id)
    .mapData { Album(it) }

private fun GetArtists.withState(
    state: TrackViewState
) = this(applySchedulers = false, args = state.track.artists.map { it.id })
    .mapData { artists -> artists.map { Artist(it) }.sortedBy { it.name } }

private fun GetSimilarTracks.withState(
    state: TrackViewState
) = this(applySchedulers = false, args = GetSimilarTracks.Args(state.track.id, state.similarTracks.offset))
    .mapData { tracks -> tracks.map { Track(it) } }

private fun GetAudioFeatures.withState(state: TrackViewState) = this(applySchedulers = false, args = state.track.id)
    .mapData {
        val entries = listOf(
            RadarEntry(it.acousticness.toFloat()),
            RadarEntry(it.danceability.toFloat()),
            RadarEntry(it.energy.toFloat()),
            RadarEntry(it.instrumentalness.toFloat()),
            RadarEntry(it.liveness.toFloat()),
            RadarEntry(it.speechiness.toFloat()),
            RadarEntry(it.valence.toFloat())
        )
        RadarData(
            RadarDataSet(entries, "Audio features")
        ).apply {
            setValueTextSize(12f)
            setDrawValues(false)
            setValueTextColor(Color.WHITE)
        }
    }
