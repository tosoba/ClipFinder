package com.clipfinder.spotify.track

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.clipfinder.core.ext.map
import com.clipfinder.core.ext.mapData
import com.clipfinder.core.spotify.ext.decimalProps
import com.clipfinder.core.spotify.model.ISpotifyAudioFeatures
import com.clipfinder.core.spotify.usecase.GetAlbum
import com.clipfinder.core.spotify.usecase.GetArtists
import com.clipfinder.core.spotify.usecase.GetAudioFeatures
import com.clipfinder.core.spotify.usecase.GetSimilarTracks
import com.clipfinder.core.android.base.viewmodel.MvRxViewModel
import com.clipfinder.core.model.PagedList
import com.clipfinder.core.android.spotify.model.Album
import com.clipfinder.core.android.spotify.model.Artist
import com.clipfinder.core.android.spotify.model.SimplifiedArtist
import com.clipfinder.core.android.spotify.model.Track
import com.clipfinder.core.android.util.ext.offset
import com.clipfinder.core.android.util.ext.retryLoadCollectionOnConnected
import com.clipfinder.core.android.util.ext.retryLoadOnNetworkAvailable
import com.clipfinder.core.model.Paged
import com.clipfinder.core.model.Resource
import com.clipfinder.core.model.WithValue
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import io.reactivex.Single
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
        if (initialState.track is WithValue) loadData()
        handleConnectivityChanges(context)
    }

    fun onNewTrack(id: String) = withState { (track) ->
        if (track is WithValue && track.value.id == id) return@withState
    }

    fun onNewTrack(newTrack: Track) = withState { (track) ->
        if (track is WithValue && track.value == newTrack) return@withState
        setState { State(track = newTrack) }
        loadData()
    }

    private fun loadData() {
        loadAlbum()
        loadArtists()
        loadSimilarTracks()
        loadAudioFeatures()
    }

    private fun withCurrentTrack(block: (Track) -> Unit) = withState { (track) ->
        if (track is WithValue) block(track.value)
    }

    fun loadAlbum() = withCurrentTrack { track ->
        load(State::album, getAlbum::withState, track) { copy(album = it) }
    }

    fun loadArtists() = withCurrentTrack { track ->
        loadCollection(State::artists, getArtists::withState, track) { copy(artists = it) }
    }

    fun clearArtistsError() = clearErrorIn(State::artists) { copy(artists = it) }

    fun loadSimilarTracks() = withCurrentTrack { track ->
        loadPaged(State::similarTracks, getSimilarTracks::withState, track, ::PagedList) {
            copy(similarTracks = it)
        }
    }

    fun clearSimilarTracksError() = clearErrorIn(State::similarTracks) { copy(similarTracks = it) }

    fun loadAudioFeatures() = withCurrentTrack { track ->
        load(State::audioFeaturesChartData, getAudioFeatures::withState, track) {
            copy(audioFeaturesChartData = it)
        }
    }

    @SuppressLint("MissingPermission")
    private fun handleConnectivityChanges(context: Context) {
        context.handleConnectivityChanges { (_, album, artists, tracks, audioFeatures) ->
            if (album.retryLoadOnNetworkAvailable) loadAlbum()
            if (artists.retryLoadCollectionOnConnected) loadArtists()
            if (tracks.retryLoadCollectionOnConnected) loadSimilarTracks()
            if (audioFeatures.retryLoadOnNetworkAvailable) loadAudioFeatures()
        }
    }

    companion object : MvRxViewModelFactory<SpotifyTrackViewModel, State> {
        override fun create(
            viewModelContext: ViewModelContext, state: State
        ): SpotifyTrackViewModel = SpotifyTrackViewModel(
            state,
            viewModelContext.activity.get(),
            viewModelContext.activity.get(),
            viewModelContext.activity.get(),
            viewModelContext.activity.get(),
            viewModelContext.app()
        )
    }
}

private fun GetAlbum.withState(
    track: Track
): Single<Resource<Album>> = this(applySchedulers = false, args = track.album.id)
    .mapData(::Album)

private fun GetArtists.withState(
    state: State, track: Track
): Single<Resource<List<Artist>>> = this(applySchedulers = false, args = track.artists.map(SimplifiedArtist::id))
    .mapData { artists -> artists.map(::Artist).sortedBy(Artist::name) }

private fun GetSimilarTracks.withState(
    state: State, track: Track
): Single<Resource<Paged<List<Track>>>> = this(applySchedulers = false, args = GetSimilarTracks.Args(track.id, state.similarTracks.offset))
    .mapData { tracks -> tracks.map(::Track) }

private fun GetAudioFeatures.withState(track: Track): Single<Resource<RadarData>> = this(applySchedulers = false, args = track.id)
    .mapData {
        RadarData(
            RadarDataSet(
                ISpotifyAudioFeatures::class.decimalProps.map { prop -> RadarEntry(prop.get(it).toFloat()) },
                "Audio features"
            )
        ).apply {
            setValueTextSize(12f)
            setDrawValues(false)
            setValueTextColor(Color.WHITE)
        }
    }
