package com.example.spotifytrack

import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.*
import com.example.coreandroid.base.IFragmentFactory
import com.example.coreandroid.base.handler.OnTrackChangeListener
import com.example.coreandroid.headerItem
import com.example.coreandroid.lifecycle.ConnectivityComponent
import com.example.coreandroid.loadingIndicator
import com.example.coreandroid.model.LoadedSuccessfully
import com.example.coreandroid.model.Loading
import com.example.coreandroid.model.LoadingFailed
import com.example.coreandroid.model.spotify.Track
import com.example.coreandroid.model.spotify.clickableListItem
import com.example.coreandroid.radarChart
import com.example.coreandroid.reloadControl
import com.example.coreandroid.util.asyncController
import com.example.coreandroid.util.carousel
import com.example.coreandroid.util.ext.NavigationCapable
import com.example.coreandroid.util.ext.reloadingConnectivityComponent
import com.example.coreandroid.util.ext.show
import com.example.coreandroid.util.withModelsFrom
import com.example.coreandroid.view.epoxy.Column
import com.example.coreandroid.view.radarchart.RadarChartAxisView
import com.example.coreandroid.view.radarchart.RadarChartView
import com.example.coreandroid.view.radarchart.RadarMarkerView
import com.example.there.domain.entity.spotify.AudioFeaturesEntity
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import kotlinx.android.synthetic.main.fragment_track.view.*
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named

class TrackFragment : BaseMvRxFragment(), NavigationCapable {

    override fun invalidate() = withState(viewModel) { state -> epoxyController.setData(state) }

    private val builder by inject<Handler>(named("builder"))
    private val differ by inject<Handler>(named("differ"))

    override val factory: IFragmentFactory by inject()

    private val viewModel: TrackViewModel by fragmentViewModel()

    private val epoxyController by lazy {
        asyncController(builder, differ, viewModel) { state ->
            headerItem {
                id("album-header")
                text("Album")
            }

            when (state.album.status) {
                is Loading -> loadingIndicator {
                    id("loading-indicator-album")
                }

                is LoadingFailed<*> -> reloadControl {
                    id("albums-reload-control")
                    onReloadClicked(View.OnClickListener {
                        track?.let { viewModel.loadAlbum(it.albumId) }
                    })
                    message("Error occurred lmao") //TODO: error msg
                }

                is LoadedSuccessfully -> {
                    state.album.value?.clickableListItem {
                        show { newSpotifyAlbumFragment(state.album.value!!) }
                    }
                }
            }

            headerItem {
                id("track-artists-header")
                text("Artists")
            }

            when (state.artists.status) {
                is Loading -> loadingIndicator {
                    id("loading-indicator-track-artists")
                }

                is LoadingFailed<*> -> reloadControl {
                    id("track-artists-reload-control")
                    onReloadClicked { _ ->
                        track?.let { track -> viewModel.loadArtists(track.artists.map { it.id }) }
                    }
                    message("Error occurred lmao") //TODO: error msg
                }

                is LoadedSuccessfully -> carousel {
                    id("track-artists")
                    withModelsFrom(state.artists.value) { artist ->
                        artist.clickableListItem {
                            show { factory.newSpotifyArtistFragment(artist) }
                        }
                    }
                }
            }

            headerItem {
                id("similar-tracks-header")
                text("Similar tracks")
            }

            when (state.similarTracks.status) {
                is Loading -> loadingIndicator {
                    id("loading-indicator-similar-tracks")
                }

                is LoadingFailed<*> -> reloadControl {
                    id("similar-tracks-reload-control")
                    onReloadClicked { _ ->
                        track?.let { track -> viewModel.loadSimilarTracks(track) }
                    }
                    message("Error occurred lmao") //TODO: error msg
                }

                is LoadedSuccessfully -> carousel {
                    id("similar-tracks")
                    withModelsFrom(state.similarTracks.value.chunked(2)) { chunk ->
                        Column(chunk.map { track ->
                            track.clickableListItem {
                                (parentFragment as? OnTrackChangeListener<Track>) //TODO: replace this with a reference to an existing TrackVideosViewModel
                                        ?.onTrackChanged(track)
                            }
                        })
                    }
                }
            }

            headerItem {
                id("audio-features-header")
                text("Audio features")
            }

            when (state.audioFeaturesChartData.status) {
                is Loading -> loadingIndicator {
                    id("loading-indicator-audio-features")
                }

                is LoadingFailed<*> -> reloadControl {
                    id("audio-features-reload-control")
                    onReloadClicked { _ ->
                        track?.let { track -> viewModel.loadAudioFeatures(track) }
                    }
                    message("Error occurred lmao") //TODO: error msg
                }

                is LoadedSuccessfully -> {
                    radarChart {
                        id("audio-features-radar-chart")
                        view(RadarChartView(
                                state.audioFeaturesChartData.value!!,
                                xAxisView = RadarChartAxisView(
                                        typeface = Typeface.createFromAsset(activity?.assets, "OpenSans-Regular.ttf"),
                                        valueFormatter = IAxisValueFormatter { value, _ ->
                                            audioFeaturesChartLabels[value.toInt() % audioFeaturesChartLabels.size]
                                        }
                                ),
                                yAxisView = RadarChartAxisView(
                                        typeface = Typeface.createFromAsset(activity?.assets, "OpenSans-Regular.ttf"),
                                        axisMaximum = 1f,
                                        drawLabels = false
                                ),
                                markerView = RadarMarkerView(context, R.layout.radar_marker_view)
                        ))
                    }
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
        this.track_recycler_view.apply {
            setController(epoxyController)
            //TODO: animation
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        track = argTrack
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(connectivityComponent)
    }

    private fun loadData() {
        track?.let { viewModel.loadData(it) }
    }

    companion object {
        fun newInstanceWithTrack(track: Track): TrackFragment = TrackFragment().apply {
            arguments = Bundle().apply { putParcelable(MvRx.KEY_ARG, track) }
        }

        private val audioFeaturesChartLabels = AudioFeaturesEntity::class.members.map { it.name }
    }
}
