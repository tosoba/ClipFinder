package com.example.spotifytrack

import android.databinding.DataBindingUtil
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.coreandroid.BR
import com.example.coreandroid.base.IFragmentFactory
import com.example.coreandroid.base.fragment.BaseVMFragment
import com.example.coreandroid.base.handler.OnTrackChangeListener
import com.example.coreandroid.di.Injectable
import com.example.coreandroid.lifecycle.ConnectivityComponent
import com.example.coreandroid.model.spotify.Artist
import com.example.coreandroid.model.spotify.Track
import com.example.coreandroid.util.ext.connectivitySnackbarHost
import com.example.coreandroid.util.ext.navHostFragment
import com.example.coreandroid.view.radarchart.RadarChartAxisView
import com.example.coreandroid.view.radarchart.RadarChartView
import com.example.coreandroid.view.radarchart.RadarMarkerView
import com.example.coreandroid.view.recyclerview.binder.ItemBinder
import com.example.coreandroid.view.recyclerview.binder.ItemBinderBase
import com.example.coreandroid.view.recyclerview.item.*
import com.example.coreandroid.view.recyclerview.listener.ClickHandler
import com.example.there.domain.entity.spotify.AudioFeaturesEntity
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import javax.inject.Inject

class TrackFragment : BaseVMFragment<TrackViewModel>(TrackViewModel::class.java), Injectable {

    @Inject
    lateinit var fragmentFactory: IFragmentFactory

    var track: Track? = null
        set(value) {
            if (field == value || value == null) return
            field = value
            viewModel.loadData(value)
            viewModel.viewState.clearAll()
        }


    private val connectivityComponent: ConnectivityComponent by lazy {
        ConnectivityComponent(
                activity!!,
                {
                    viewModel.viewState.album.get() != null &&
                            viewModel.viewState.artists.isNotEmpty() &&
                            viewModel.viewState.similarTracks.isNotEmpty()
                },
                connectivitySnackbarHost!!.connectivitySnackbarParentView!!,
                ::loadData,
                true
        )
    }

    private val trackAdapter: TrackAdapter by lazy {
        TrackAdapter(
                AlbumInfoItemView(
                        AlbumInfoViewState(viewModel.viewState.albumLoadingInProgress, viewModel.viewState.album),
                        View.OnClickListener { _ ->
                            val album = viewModel.viewState.album.get()
                            album?.let {
                                navHostFragment?.showFragment(
                                    fragmentFactory.newSpotifyAlbumFragment(album = it),
                                    true
                                )
                            }
                        }
                ),
                RecyclerViewItemView(
                        RecyclerViewItemViewState(viewModel.viewState.artistsLoadingInProgress, viewModel.viewState.artists, viewModel.viewState.artistsLoadingErrorOccurred),
                        object : ListItemView<Artist>(viewModel.viewState.artists) {
                            override val itemViewBinder: ItemBinder<Artist>
                                get() = ItemBinderBase(BR.imageListItem, R.layout.named_image_list_item)
                        },
                        ClickHandler {
                            navHostFragment?.showFragment(fragmentFactory.newSpotifyArtistFragment(artist = it), true)
                        },
                        onReloadBtnClickListener = View.OnClickListener { _ ->
                            track?.let { viewModel.loadArtists(it.artists.map { artist -> artist.id }) }
                        }
                ),
                RecyclerViewItemView(
                        RecyclerViewItemViewState(viewModel.viewState.similarTracksLoadingInProgress, viewModel.viewState.similarTracks, viewModel.viewState.similarTracksErrorOccurred),
                        object : ListItemView<Track>(viewModel.viewState.similarTracks) {
                            override val itemViewBinder: ItemBinder<Track>
                                get() = ItemBinderBase(BR.imageListItem, R.layout.named_image_list_item)
                        },
                        ClickHandler {
                            (parentFragment as? OnTrackChangeListener)?.onTrackChanged(newTrack = it)
                        },
                        onReloadBtnClickListener = View.OnClickListener { _ ->
                            track?.let { viewModel.loadSimilarTracks(it) }
                        }
                ),
                RadarChartView(
                        viewModel.viewState.audioFeaturesChartData,
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
                )
        )
    }

    private val view: TrackView by lazy {
        TrackView(viewModel.viewState, trackAdapter)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = DataBindingUtil.inflate<com.example.spotifytrack.databinding.FragmentTrackBinding>(
            inflater,
            R.layout.fragment_track,
            container,
            false
    ).apply {
        view = this@TrackFragment.view
        trackRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }.root

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        track = arguments?.getParcelable(ARG_TRACK)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(connectivityComponent)
    }

    private fun loadData() {
        track?.let { viewModel.loadData(it) }
    }

    companion object {
        private const val ARG_TRACK = "ARG_TRACK"

        fun newInstanceWithTrack(track: Track): TrackFragment = TrackFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_TRACK, track)
            }
        }

        private val audioFeaturesChartLabels = AudioFeaturesEntity::class.members.map { it.name }
    }
}
