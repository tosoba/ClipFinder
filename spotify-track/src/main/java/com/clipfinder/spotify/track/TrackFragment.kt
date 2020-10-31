package com.clipfinder.spotify.track

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.example.core.android.headerItem
import com.example.core.android.loadingIndicator
import com.example.core.android.model.Initial
import com.example.core.android.model.LoadedSuccessfully
import com.example.core.android.model.Loading
import com.example.core.android.model.LoadingFailed
import com.example.core.android.radarChart
import com.example.core.android.reloadControl
import com.example.core.android.spotify.controller.SpotifyTrackController
import com.example.core.android.spotify.fragment.ISpotifyTrackFragment
import com.example.core.android.spotify.model.Track
import com.example.core.android.spotify.model.clickableListItem
import com.example.core.android.spotify.model.infoItem
import com.example.core.android.spotify.navigation.ISpotifyFragmentsFactory
import com.example.core.android.util.ext.findAncestorFragmentOfType
import com.example.core.android.util.ext.newMvRxFragmentWith
import com.example.core.android.util.ext.show
import com.example.core.android.view.epoxy.Column
import com.example.core.android.view.epoxy.dataListCarouselWithHeader
import com.example.core.android.view.epoxy.injectedTypedController
import com.example.core.android.view.epoxy.pagedDataListCarouselWithHeader
import com.example.core.android.view.radarchart.RadarChartAxisView
import com.example.core.android.view.radarchart.RadarChartView
import com.example.core.android.view.radarchart.RadarMarkerView
import com.example.there.domain.entity.spotify.AudioFeaturesEntity
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import kotlinx.android.synthetic.main.fragment_track.view.*
import org.koin.android.ext.android.inject

class TrackFragment : BaseMvRxFragment(), ISpotifyTrackFragment {
    private val factory: ISpotifyFragmentsFactory by inject()
    private val viewModel: TrackViewModel by fragmentViewModel()

    private val epoxyController: TypedEpoxyController<TrackViewState> by lazy(LazyThreadSafetyMode.NONE) {
        injectedTypedController<TrackViewState> { (_, album, artists, similarTracks, audioFeaturesChartData) ->
            headerItem {
                id("album-header")
                text("Album")
            }

            when (album.status) {
                is Initial, Loading -> loadingIndicator { id("loading-indicator-album") }

                is LoadingFailed<*> -> reloadControl {
                    id("albums-reload-control")
                    onReloadClicked { _ -> viewModel.loadAlbum() }
                    message("Error occurred lmao") //TODO: error msg
                }

                is LoadedSuccessfully -> requireNotNull(album.value).let {
                    it.infoItem { show { factory.newSpotifyAlbumFragment(it) } }
                        .addTo(this)
                }
            }

            dataListCarouselWithHeader(
                requireContext(),
                artists,
                R.string.artists,
                "track-artists",
                viewModel::loadArtists
            ) { artist ->
                artist.clickableListItem {
                    show { factory.newSpotifyArtistFragment(artist) }
                }
            }

            pagedDataListCarouselWithHeader(
                requireContext(),
                similarTracks,
                R.string.similar_tracks,
                "similar-tracks",
                viewModel::loadSimilarTracks,
                { it.chunked(2) }
            ) { chunk ->
                Column(chunk.map { track ->
                    track.clickableListItem {
                        findAncestorFragmentOfType<SpotifyTrackController>()?.updateTrack(track)
                    }
                })
            }

            headerItem {
                id("audio-features-header")
                text("Audio features")
            }

            when (audioFeaturesChartData.status) {
                is Initial, Loading -> loadingIndicator { id("loading-indicator-audio-features") }

                is LoadingFailed<*> -> reloadControl {
                    id("audio-features-reload-control")
                    onReloadClicked { _ -> viewModel.loadAudioFeatures() }
                    message("Error occurred lmao") //TODO: error msg
                }

                is LoadedSuccessfully -> radarChart {
                    val typeface = Typeface.createFromAsset(activity?.assets, "OpenSans-Regular.ttf")
                    id("audio-features-radar-chart")
                    view(
                        RadarChartView(
                            requireNotNull(audioFeaturesChartData.value),
                            xAxisView = RadarChartAxisView(
                                typeface = typeface,
                                valueFormatter = IAxisValueFormatter { value, _ ->
                                    audioFeaturesChartLabels[value.toInt() % audioFeaturesChartLabels.size]
                                }
                            ),
                            yAxisView = RadarChartAxisView(
                                typeface = typeface,
                                axisMaximum = 1f,
                                drawLabels = false
                            ),
                            markerView = RadarMarkerView(context, R.layout.radar_marker_view)
                        )
                    )
                }
            }
        }
    }

    override fun onNewTrack(track: Track) = viewModel.onNewTrack(track)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_track, container, false).apply {
        this.track_recycler_view.setController(epoxyController)
    }

    override fun invalidate() = withState(viewModel, epoxyController::setData)

    companion object {
        fun new(track: Track): TrackFragment = newMvRxFragmentWith(track)

        private val audioFeaturesChartLabels = AudioFeaturesEntity::class.members.map { it.name }
    }
}
