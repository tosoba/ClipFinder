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
import com.example.core.ext.map
import com.example.core.ext.mapData
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import org.koin.android.ext.android.get

private typealias State = SpotifyTrackViewState

class SpotifyTrackViewModel(
    initialState: State,
    private val getAlbum: GetAlbum,
    private val getArtists: GetArtists,
    private val getSimilarTracks: GetSimilarTracks,
    private val getAudioFeatures: GetAudioFeatures,
    context: Context
) : MvRxViewModel<State>(initialState) {

    init {
        loadData()
        handleConnectivityChanges(context)
    }

    fun onNewTrack(newTrack: Track) = withState { (track) ->
        if (track == newTrack) return@withState
        setState { State(track = newTrack) }
        loadData()
    }

    private fun loadData() {
        loadAlbum()
        loadArtists()
        loadSimilarTracks()
        loadAudioFeatures()
    }

    fun loadAlbum() = loadNullable(State::album, getAlbum::withState) { copy(album = it) }

    fun loadArtists() = load(State::artists, getArtists::withState) { copy(artists = it) }

    fun loadSimilarTracks() = load(State::similarTracks, getSimilarTracks::withState) { copy(similarTracks = it) }

    fun loadAudioFeatures() = loadNullable(State::audioFeaturesChartData, getAudioFeatures::withState) {
        copy(audioFeaturesChartData = it)
    }

    @SuppressLint("MissingPermission")
    private fun handleConnectivityChanges(context: Context) {
        context.handleConnectivityChanges { (_, album, artists, tracks, audioFeatures) ->
            if (album.retryLoadOnNetworkAvailable) loadAlbum()
            if (artists.retryLoadItemsOnNetworkAvailable) loadArtists()
            if (tracks.retryLoadItemsOnNetworkAvailable) loadSimilarTracks()
            if (audioFeatures.retryLoadOnNetworkAvailable) loadAudioFeatures()
        }
    }

    companion object : MvRxViewModelFactory<SpotifyTrackViewModel, State> {
        override fun create(viewModelContext: ViewModelContext, state: State): SpotifyTrackViewModel? = SpotifyTrackViewModel(
            state,
            viewModelContext.activity.get(),
            viewModelContext.activity.get(),
            viewModelContext.activity.get(),
            viewModelContext.activity.get(),
            viewModelContext.app()
        )
    }
}

private fun GetAlbum.withState(state: State) = this(applySchedulers = false, args = state.track.album.id)
    .mapData(::Album)

private fun GetArtists.withState(state: State) = this(applySchedulers = false, args = state.track.artists.map { it.id })
    .mapData { artists -> artists.map(::Artist).sortedBy(Artist::name) }

private fun GetSimilarTracks.withState(
    state: State
) = this(applySchedulers = false, args = GetSimilarTracks.Args(state.track.id, state.similarTracks.offset))
    .mapData { tracks -> tracks.map(::Track) }

private fun GetAudioFeatures.withState(state: State) = this(applySchedulers = false, args = state.track.id)
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
