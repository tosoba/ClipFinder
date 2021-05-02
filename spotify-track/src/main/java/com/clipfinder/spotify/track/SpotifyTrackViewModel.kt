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
import com.clipfinder.core.android.base.viewmodel.MvRxViewModel
import com.clipfinder.core.model.PagedList
import com.clipfinder.core.android.spotify.model.Artist
import com.clipfinder.core.android.spotify.model.SimplifiedArtist
import com.clipfinder.core.android.spotify.model.Track
import com.clipfinder.core.android.util.ext.offset
import com.clipfinder.core.android.util.ext.retryLoadCollectionOnConnected
import com.clipfinder.core.android.util.ext.retryLoadOnNetworkAvailable
import com.clipfinder.core.model.Paged
import com.clipfinder.core.model.Resource
import com.clipfinder.core.model.WithValue
import com.clipfinder.core.spotify.usecase.*
import com.clipfinder.core.model.invoke
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import org.koin.android.ext.android.get

private typealias State = SpotifyTrackViewState

class SpotifyTrackViewModel(
    initialState: State,
    private val getTrack: GetTrack,
    private val getArtists: GetArtists,
    private val getSimilarTracks: GetSimilarTracks,
    private val getAudioFeatures: GetAudioFeatures,
    context: Context
) : MvRxViewModel<State>(initialState) {
    private val clearLoadableTrackSubscription: PublishRelay<Unit> = PublishRelay.create()

    init {
        if (initialState.track is WithValue) loadData(initialState.track.value)
        handleConnectivityChanges(context)
    }

    fun onNewTrack(id: String) = withState { (trackId) ->
        if (trackId is WithValue && trackId.value == id) return@withState
        clearLoadableTrackSubscription.accept(Unit)
        loadTrack(id)
        loadSimilarTracksFor(id)
        loadAudioFeaturesFor(id)

        var trackDisposable: Disposable? = null
        trackDisposable = selectSubscribe(State::track) { track ->
            if (track is WithValue) {
                loadArtistsFor(track.value)
                trackDisposable?.dispose()
            }
        }
        clearLoadableTrackSubscription
            .take(1)
            .subscribe { trackDisposable.dispose() }
            .disposeOnClear()
    }

    fun onNewTrack(newTrack: Track) = withState { (_, track) ->
        if (track is WithValue && track.value == newTrack) return@withState
        setState { State(track = newTrack) }
        loadData(newTrack)
    }

    private fun loadData(track: Track) {
        loadArtistsFor(track)
        loadSimilarTracksFor(track.id)
        loadAudioFeaturesFor(track.id)
    }

    private fun withCurrentTrack(block: (Track) -> Unit) = withState { (_, track) ->
        if (track is WithValue) block(track.value)
    }

    fun loadTrack() = withState { (trackId) -> if (trackId is WithValue) loadTrack(trackId.value) }

    private fun loadTrack(id: String) {
        load(State::track, getTrack::with, id) { copy(track = it) }
    }

    fun loadArtists() = withCurrentTrack(::loadArtistsFor)

    private fun loadArtistsFor(track: Track) {
        loadCollection(State::artists, getArtists::with, track) { copy(artists = it) }
    }

    fun clearArtistsError() = clearErrorIn(State::artists) { copy(artists = it) }

    fun loadSimilarTracks() = withCurrentTrack { track -> loadSimilarTracksFor(track.id) }

    private fun loadSimilarTracksFor(trackId: String) {
        loadPaged(State::similarTracks, getSimilarTracks::with, trackId, ::PagedList) {
            copy(similarTracks = it)
        }
    }

    fun clearSimilarTracksError() = clearErrorIn(State::similarTracks) { copy(similarTracks = it) }

    fun loadAudioFeatures() = withCurrentTrack { track -> loadAudioFeaturesFor(track.id) }

    private fun loadAudioFeaturesFor(trackId: String) {
        load(State::audioFeaturesChartData, getAudioFeatures::with, trackId) {
            copy(audioFeaturesChartData = it)
        }
    }

    @SuppressLint("MissingPermission")
    private fun handleConnectivityChanges(context: Context) {
        context.handleConnectivityChanges { (trackId, track, artists, tracks, audioFeatures) ->
            if (track.retryLoadOnNetworkAvailable && trackId is WithValue) loadTrack(trackId.value)
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

private fun GetTrack.with(id: String): Single<Resource<Track>> = this(args = id)
    .mapData(::Track)

private fun GetArtists.with(state: State, track: Track): Single<Resource<List<Artist>>> =
    this(args = track.artists.map(SimplifiedArtist::id))
        .mapData { artists -> artists.map(::Artist).sortedBy(Artist::name) }

private fun GetSimilarTracks.with(state: State, trackId: String): Single<Resource<Paged<List<Track>>>> =
    this(args = GetSimilarTracks.Args(trackId, state.similarTracks.offset))
        .mapData { tracks -> tracks.map(::Track) }

private fun GetAudioFeatures.with(trackId: String): Single<Resource<RadarData>> =
    this(args = trackId)
        .mapData {
            RadarData(
                RadarDataSet(
                    ISpotifyAudioFeatures::class
                        .decimalProps
                        .map { prop -> RadarEntry(prop.get(it).toFloat()) },
                    "Audio features"
                )
            ).apply {
                setValueTextSize(12f)
                setDrawValues(false)
                setValueTextColor(Color.WHITE)
            }
        }
