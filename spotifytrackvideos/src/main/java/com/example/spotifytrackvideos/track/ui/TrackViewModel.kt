package com.example.spotifytrackvideos.track.ui

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.clipfinder.core.spotify.usecase.GetAlbum
import com.clipfinder.core.spotify.usecase.GetArtists
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.model.DataList
import com.example.core.android.model.Loading
import com.example.core.android.spotify.model.Album
import com.example.core.android.spotify.model.Artist
import com.example.core.android.spotify.model.Track
import com.example.core.model.mapData
import com.example.there.domain.usecase.spotify.GetAudioFeatures
import com.example.there.domain.usecase.spotify.GetSimilarTracks
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
        loadAlbum(track.album.id)
        loadArtists(artistIds = track.artists.map { it.id })
//        loadSimilarTracks(track)
//        loadAudioFeatures(track)
    }

    fun clear() {
        setState { copy(artists = DataList(), similarTracks = DataList()) }
    }

    fun loadAlbum(albumId: String) = withState { state ->
        if (state.album.status is Loading) return@withState

        getAlbum(args = albumId, applySchedulers = false)
            .subscribeOn(Schedulers.io())
            .mapData { Album(it) }
            .updateNullableWithSingleResource(TrackViewState::album) { copy(album = it) }
    }

    fun loadArtists(artistIds: List<String>) = withState { state ->
        if (state.artists.status is Loading) return@withState

        getArtists(args = artistIds, applySchedulers = false)
            .subscribeOn(Schedulers.io())
            .mapData { artists -> artists.map { Artist(it) }.sortedBy { it.name } }
            .updateWithResource(TrackViewState::artists) { copy(artists = it) }
    }
//
//    fun loadSimilarTracks(track: Track) = withState { state ->
//        if (state.similarTracks.status is Loading) return@withState
//
//        getSimilarTracks(args = track.id, applySchedulers = false)
//            .subscribeOn(Schedulers.io())
//            .mapData { tracks -> tracks.map(TrackEntity::ui) }
//            .updateWithResource(TrackViewState::similarTracks) { copy(similarTracks = it) }
//    }
//
//    fun loadAudioFeatures(track: Track) {
//        withState { state ->
//            if (state.audioFeaturesChartData.status is Loading) return@withState
//
//            getAudioFeatures(args = track.domain, applySchedulers = false)
//                .subscribeOn(Schedulers.io())
//                .mapData {
//                    val entries = listOf(
//                        RadarEntry(it.acousticness),
//                        RadarEntry(it.danceability),
//                        RadarEntry(it.energy),
//                        RadarEntry(it.instrumentalness),
//                        RadarEntry(it.liveness),
//                        RadarEntry(it.speechiness),
//                        RadarEntry(it.valence)
//                    )
//                    RadarData(
//                        RadarDataSet(entries, "Audio features")
//                    ).apply {
//                        setValueTextSize(12f)
//                        setDrawValues(false)
//                        setValueTextColor(Color.WHITE)
//                    }
//                }
//                .updateNullableWithSingleResource(TrackViewState::audioFeaturesChartData) {
//                    copy(audioFeaturesChartData = it)
//                }
//        }
//    }

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
