package com.example.spotifytrackvideos.track

import android.graphics.Color
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.model.mapData
import com.example.coreandroid.base.vm.MvRxViewModel
import com.example.coreandroid.mapper.spotify.domain
import com.example.coreandroid.mapper.spotify.ui
import com.example.coreandroid.model.DataList
import com.example.coreandroid.model.Loading
import com.example.coreandroid.model.spotify.Track
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.usecase.spotify.GetAlbum
import com.example.there.domain.usecase.spotify.GetArtists
import com.example.there.domain.usecase.spotify.GetAudioFeatures
import com.example.there.domain.usecase.spotify.GetSimilarTracks
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
        loadAlbum(track.albumId)
        loadArtists(artistIds = track.artists.map { it.id })
        loadSimilarTracks(track)
        loadAudioFeatures(track)
    }

    fun clear() {
        setState { copy(artists = DataList(), similarTracks = DataList()) }
    }

    fun loadAlbum(albumId: String) = withState { state ->
        if (state.album.status is Loading) return@withState

        getAlbum(args = albumId, applySchedulers = false)
                .subscribeOn(Schedulers.io())
                .mapData(AlbumEntity::ui)
                .updateNullableWithSingleResource(TrackViewState::album) {
                    copy(album = it)
                }
    }

    fun loadArtists(artistIds: List<String>) = withState { state ->
        if (state.artists.status is Loading) return@withState

        getArtists(args = artistIds, applySchedulers = false)
                .subscribeOn(Schedulers.io())
                .mapData { artists -> artists.map(ArtistEntity::ui) }
                .updateWithResource(TrackViewState::artists) { copy(artists = it) }
    }

    fun loadSimilarTracks(track: Track) = withState { state ->
        if (state.similarTracks.status is Loading) return@withState

        getSimilarTracks(args = track.id, applySchedulers = false)
                .subscribeOn(Schedulers.io())
                .mapData { tracks -> tracks.map(TrackEntity::ui) }
                .updateWithResource(TrackViewState::similarTracks) { copy(similarTracks = it) }
    }

    fun loadAudioFeatures(track: Track) {
        withState { state ->
            if (state.audioFeaturesChartData.status is Loading) return@withState

            getAudioFeatures(args = track.domain, applySchedulers = false)
                    .subscribeOn(Schedulers.io())
                    .mapData {
                        val entries = listOf(
                                RadarEntry(it.acousticness),
                                RadarEntry(it.danceability),
                                RadarEntry(it.energy),
                                RadarEntry(it.instrumentalness),
                                RadarEntry(it.liveness),
                                RadarEntry(it.speechiness),
                                RadarEntry(it.valence)
                        )
                        RadarData(
                                RadarDataSet(entries, "Audio features")
                        ).apply {
                            setValueTextSize(12f)
                            setDrawValues(false)
                            setValueTextColor(Color.WHITE)
                        }
                    }
                    .updateNullableWithSingleResource(TrackViewState::audioFeaturesChartData) { copy(audioFeaturesChartData = it) }
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