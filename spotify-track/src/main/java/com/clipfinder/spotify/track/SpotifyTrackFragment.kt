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
import com.clipfinder.core.android.headerItem
import com.clipfinder.core.android.loadingIndicator
import com.clipfinder.core.model.Failed
import com.clipfinder.core.model.LoadingInProgress
import com.clipfinder.core.model.Ready
import com.clipfinder.core.android.radarChart
import com.clipfinder.core.android.reloadControl
import com.clipfinder.core.android.spotify.base.SpotifyTrackController
import com.clipfinder.core.android.spotify.fragment.ISpotifyTrackFragment
import com.clipfinder.core.android.spotify.model.Track
import com.clipfinder.core.android.spotify.model.clickableListItem
import com.clipfinder.core.android.spotify.model.infoItem
import com.clipfinder.core.android.spotify.navigation.ISpotifyFragmentsFactory
import com.clipfinder.core.android.util.ext.findAncestorFragmentOfType
import com.clipfinder.core.android.util.ext.newMvRxFragmentWith
import com.clipfinder.core.android.util.ext.show
import com.clipfinder.core.android.view.epoxy.Column
import com.clipfinder.core.android.view.epoxy.injectedTypedController
import com.clipfinder.core.android.view.epoxy.loadableCarouselWithHeader
import com.clipfinder.core.android.view.radarchart.RadarChartAxisView
import com.clipfinder.core.android.view.radarchart.RadarChartView
import com.clipfinder.core.android.view.radarchart.RadarMarkerView
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import kotlinx.android.synthetic.main.fragment_spotify_track.view.*
import org.koin.android.ext.android.inject
import java.math.BigDecimal
import kotlin.reflect.KCallable

class SpotifyTrackFragment : BaseMvRxFragment(), ISpotifyTrackFragment {
    private val factory: ISpotifyFragmentsFactory by inject()
    private val viewModel: SpotifyTrackViewModel by fragmentViewModel()

    private val epoxyController: TypedEpoxyController<SpotifyTrackViewState> by lazy(LazyThreadSafetyMode.NONE) {
        injectedTypedController<SpotifyTrackViewState> { (_, track, artists, similarTracks, audioFeaturesChartData) ->
            headerItem {
                id("album-header")
                text("Album")
            }

            when (track) {
                is LoadingInProgress -> loadingIndicator { id("loading-indicator-album") }

                is Failed -> reloadControl {
                    id("albums-reload-control")
                    onReloadClicked { _ -> viewModel.loadTrack() }
                    message(requireContext().getString(R.string.error_occurred))
                }

                is Ready -> track.value.album
                    .infoItem { show { factory.newSpotifyAlbumFragment(track.value.album) } }
                    .addTo(this)
            }

            loadableCarouselWithHeader(
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

            loadableCarouselWithHeader(
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

    fun onNewTrack(id: String) = viewModel.onNewTrack(id)

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
