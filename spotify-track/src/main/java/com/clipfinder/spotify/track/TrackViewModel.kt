package com.clipfinder.spotify.track

import android.graphics.Color
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.clipfinder.core.spotify.usecase.GetAlbum
import com.clipfinder.core.spotify.usecase.GetArtists
import com.clipfinder.core.spotify.usecase.GetAudioFeatures
import com.clipfinder.core.spotify.usecase.GetSimilarTracks
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.model.DataList
import com.example.core.android.model.Loading
import com.example.core.android.model.PagedDataList
import com.example.core.android.spotify.model.Album
import com.example.core.android.spotify.model.Artist
import com.example.core.android.spotify.model.Track
import com.example.core.model.map
import com.example.core.model.mapData
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class TrackViewModel(
    initialState: TrackViewState,
    private val getAlbum: GetAlbum,
    private val getArtists: GetArtists,
    private val getSimilarTracks: GetSimilarTracks,
    private val getAudioFeatures: GetAudioFeatures
) : MvRxViewModel<TrackViewState>(initialState) {

    fun loadData(track: Track) {
        setState { copy(track = track) }
        loadAlbum(track.album.id)
        loadArtists(artistIds = track.artists.map { it.id })
        loadSimilarTracks(track.id)
        loadAudioFeatures(track.id)
    }

    fun clear() {
        setState { copy(artists = DataList(), similarTracks = PagedDataList()) }
    }

    fun loadAlbum(id: String) = loadNullable(TrackViewState::album, getAlbum::withId, id) {
        copy(album = it)
    }

    fun loadArtists(artistIds: List<String>) = withState { state ->
        if (state.artists.status is Loading) return@withState

        getArtists(args = artistIds, applySchedulers = false)
            .subscribeOn(Schedulers.io())
            .mapData { artists -> artists.map { Artist(it) }.sortedBy { it.name } }
            .updateWithResource(TrackViewState::artists) { copy(artists = it) }
    }

    fun loadSimilarTracks(trackId: String) = withState { state ->
        if (state.similarTracks.status is Loading) return@withState

        val args = GetSimilarTracks.Args(trackId, state.similarTracks.offset)
        getSimilarTracks(args = args, applySchedulers = false)
            .subscribeOn(Schedulers.io())
            .mapData { tracks -> tracks.map { Track(it) } }
            .updateWithPagedResource(TrackViewState::similarTracks) { copy(similarTracks = it) }
    }

    fun loadAudioFeatures(trackId: String) {
        loadNullable(TrackViewState::audioFeaturesChartData, getAudioFeatures::forTrackWithId, trackId) {
            copy(audioFeaturesChartData = it)
        }
    }

    companion object : MvRxViewModelFactory<TrackViewModel, TrackViewState> {
        override fun create(viewModelContext: ViewModelContext, state: TrackViewState): TrackViewModel? {
            val getAlbum: GetAlbum by viewModelContext.activity.inject()
            val getArtists: GetArtists by viewModelContext.activity.inject()
            val getSimilarTracks: GetSimilarTracks by viewModelContext.activity.inject()
            val getAudioFeatures: GetAudioFeatures by viewModelContext.activity.inject()
            return TrackViewModel(state, getAlbum, getArtists, getSimilarTracks, getAudioFeatures)
        }
    }
}

private fun GetAlbum.withId(albumId: String) = this(applySchedulers = false, args = albumId)
    .mapData { Album(it) }

private fun GetAudioFeatures.forTrackWithId(trackId: String) = this(applySchedulers = false, args = trackId)
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
