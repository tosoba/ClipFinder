package com.example.spotifytrackvideos.track.ui

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.args
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.example.core.android.headerItem
import com.example.core.android.lifecycle.ConnectivityComponent
import com.example.core.android.loadingIndicator
import com.example.core.android.model.LoadedSuccessfully
import com.example.core.android.model.Loading
import com.example.core.android.model.LoadingFailed
import com.example.core.android.radarChart
import com.example.core.android.reloadControl
import com.example.core.android.spotify.model.Track
import com.example.core.android.spotify.model.clickableListItem
import com.example.core.android.spotify.model.infoItem
import com.example.core.android.spotify.navigation.ISpotifyFragmentsFactory
import com.example.core.android.util.ext.newFragmentWithMvRxArg
import com.example.core.android.util.ext.parentFragmentViewModel
import com.example.core.android.util.ext.reloadingConnectivityComponent
import com.example.core.android.util.ext.show
import com.example.core.android.view.epoxy.Column
import com.example.core.android.view.epoxy.dataListCarouselWithHeader
import com.example.core.android.view.epoxy.injectedTypedController
import com.example.core.android.view.radarchart.RadarChartAxisView
import com.example.core.android.view.radarchart.RadarChartView
import com.example.core.android.view.radarchart.RadarMarkerView
import com.example.spotifytrackvideos.R
import com.example.spotifytrackvideos.TrackVideosViewModel
import com.example.there.domain.entity.spotify.AudioFeaturesEntity
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import kotlinx.android.synthetic.main.fragment_track.view.*
import org.koin.android.ext.android.inject

class TrackFragment : BaseMvRxFragment() {

    private val factory: ISpotifyFragmentsFactory by inject()

    private val viewModel: TrackViewModel by fragmentViewModel()
    private val parentViewModel: TrackVideosViewModel by parentFragmentViewModel()

    private val epoxyController by lazy(LazyThreadSafetyMode.NONE) {
        injectedTypedController<TrackViewState> { (album, artists, similarTracks, audioFeaturesChartData) ->
            headerItem {
                id("album-header")
                text("Album")
            }

            when (album.status) {
                is Loading -> loadingIndicator {
                    id("loading-indicator-album")
                }

                is LoadingFailed<*> -> reloadControl {
                    id("albums-reload-control")
                    onReloadClicked(View.OnClickListener {
                        track?.let { viewModel.loadAlbum(it.album.id) }
                    })
                    message("Error occurred lmao") //TODO: error msg
                }

                is LoadedSuccessfully -> album.value?.let {
                    it.infoItem { show { factory.newSpotifyAlbumFragment(it) } }
                        .addTo(this)
                } ?: Unit
            }

            dataListCarouselWithHeader(
                requireContext(),
                artists,
                R.string.artists,
                "track-artists",
                { track?.let { track -> viewModel.loadArtists(track.artists.map { it.id }) } }
            ) { artist ->
                artist.clickableListItem {
                    show { factory.newSpotifyArtistFragment(artist) }
                }
            }

            dataListCarouselWithHeader(
                requireContext(),
                similarTracks,
                R.string.similar_tracks,
                "similar-tracks",
                {
                    track?.let { track ->
//                    viewModel.loadSimilarTracks(track)
                    }
                },
                { it.chunked(2) }
            ) { chunk ->
                Column(chunk.map { track ->
                    track.clickableListItem { parentViewModel.updateTrack(track) }
                })
            }

            headerItem {
                id("audio-features-header")
                text("Audio features")
            }

            when (audioFeaturesChartData.status) {
                is Loading -> loadingIndicator {
                    id("loading-indicator-audio-features")
                }

                is LoadingFailed<*> -> reloadControl {
                    id("audio-features-reload-control")
                    onReloadClicked { _ ->
                        track?.let { track ->
//                            viewModel.loadAudioFeatures(track)
                        }
                    }
                    message("Error occurred lmao") //TODO: error msg
                }

                is LoadedSuccessfully -> radarChart {
                    val typeface = Typeface.createFromAsset(activity?.assets, "OpenSans-Regular.ttf")
                    id("audio-features-radar-chart")
                    view(
                        RadarChartView(
                            audioFeaturesChartData.value!!,
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

    //TODO: move this to viewState - since it won't get recreated
    var track: Track? = null
        set(value) {
            if (field == value || value == null) return
            field = value
            viewModel.clear()
            viewModel.loadData(value)
        }

    private val argTrack: Track by args()

    private val connectivityComponent: ConnectivityComponent by lazy {
        reloadingConnectivityComponent(::loadData) {
            withState(viewModel) {
                it.album.status is LoadingFailed<*> ||
                    it.artists.status is LoadingFailed<*> ||
                    it.similarTracks.status is LoadingFailed<*> ||
                    it.audioFeaturesChartData.status is LoadingFailed<*>
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_track, container, false).apply {
        this.track_recycler_view.setController(epoxyController)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        track = argTrack
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(connectivityComponent)
    }

    override fun invalidate() = withState(viewModel, epoxyController::setData)

    private fun loadData() {
        track?.let(viewModel::loadData)
    }

    companion object {
        fun new(track: Track): TrackFragment = newFragmentWithMvRxArg(track)

        private val audioFeaturesChartLabels = AudioFeaturesEntity::class.members.map { it.name }
    }
}
