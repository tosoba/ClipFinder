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
import com.clipfinder.core.spotify.ext.decimalProps
import com.clipfinder.core.spotify.model.ISpotifyAudioFeatures
import com.example.core.android.headerItem
import com.example.core.android.loadingIndicator
import com.example.core.android.model.Failed
import com.example.core.android.model.LoadingInProgress
import com.example.core.android.model.Ready
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
import com.example.core.android.view.epoxy.injectedTypedController
import com.example.core.android.view.epoxy.defaultLoadableCarouselWithHeader
import com.example.core.android.view.radarchart.RadarChartAxisView
import com.example.core.android.view.radarchart.RadarChartView
import com.example.core.android.view.radarchart.RadarMarkerView
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import kotlinx.android.synthetic.main.fragment_spotify_track.view.*
import org.koin.android.ext.android.inject
import java.math.BigDecimal
import kotlin.reflect.KCallable

class SpotifyTrackFragment : BaseMvRxFragment(), ISpotifyTrackFragment {
    private val factory: ISpotifyFragmentsFactory by inject()
    private val viewModel: SpotifyTrackViewModel by fragmentViewModel()

    private val epoxyController: TypedEpoxyController<SpotifyTrackViewState> by lazy(LazyThreadSafetyMode.NONE) {
        injectedTypedController<SpotifyTrackViewState> { (_, album, artists, similarTracks, audioFeaturesChartData) ->
            headerItem {
                id("album-header")
                text("Album")
            }

            when (album) {
                is LoadingInProgress -> loadingIndicator { id("loading-indicator-album") }

                is Failed -> reloadControl {
                    id("albums-reload-control")
                    onReloadClicked { _ -> viewModel.loadAlbum() }
                    message(requireContext().getString(R.string.error_occurred))
                }

                is Ready -> album.value
                    .infoItem { show { factory.newSpotifyAlbumFragment(album.value) } }
                    .addTo(this)
            }

            defaultLoadableCarouselWithHeader(
                requireContext(),
                artists,
                R.string.artists,
                "track-artists",
                viewModel::loadArtists,
                viewModel::clearArtistsError
            ) { artist ->
                artist.clickableListItem {
                    show { factory.newSpotifyArtistFragment(artist) }
                }
            }

            defaultLoadableCarouselWithHeader(
                requireContext(),
                similarTracks,
                R.string.similar_tracks,
                "similar-tracks",
                viewModel::loadSimilarTracks,
                viewModel::clearSimilarTracksError,
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

            when (audioFeaturesChartData) {
                is LoadingInProgress -> loadingIndicator { id("loading-indicator-audio-features") }

                is Failed -> reloadControl {
                    id("audio-features-reload-control")
                    onReloadClicked { _ -> viewModel.loadAudioFeatures() }
                    message(requireContext().getString(R.string.error_occurred))
                }

                is Ready -> radarChart {
                    val typeface = Typeface.createFromAsset(activity?.assets, "OpenSans-Regular.ttf")
                    id("audio-features-radar-chart")
                    view(
                        RadarChartView(
                            audioFeaturesChartData.value,
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
    ): View? = inflater.inflate(R.layout.fragment_spotify_track, container, false)
        .apply { this.track_recycler_view.setController(epoxyController) }

    override fun invalidate() = withState(viewModel, epoxyController::setData)

    companion object {
        fun new(track: Track): SpotifyTrackFragment = newMvRxFragmentWith(track)

        private val audioFeaturesChartLabels: List<String> = ISpotifyAudioFeatures::class
            .decimalProps
            .map(KCallable<BigDecimal>::name)
    }
}
