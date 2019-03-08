package com.example.there.findclips.spotify.spotifyitem.track

import android.databinding.DataBindingUtil
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.databinding.library.baseAdapters.BR
import com.example.there.domain.entity.spotify.AudioFeaturesEntity
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseVMFragment
import com.example.there.findclips.databinding.FragmentTrackBinding
import com.example.there.findclips.di.Injectable
import com.example.there.findclips.lifecycle.ConnectivityComponent
import com.example.there.findclips.model.entity.spotify.Artist
import com.example.there.findclips.model.entity.spotify.Track
import com.example.there.findclips.spotify.spotifyitem.album.AlbumFragment
import com.example.there.findclips.spotify.spotifyitem.artist.ArtistFragment
import com.example.there.findclips.spotify.trackvideos.OnTrackChangeListener
import com.example.there.findclips.util.ext.connectivitySnackbarHost
import com.example.there.findclips.util.ext.navHostFragment
import com.example.there.findclips.view.list.ClickHandler
import com.example.there.findclips.view.list.binder.ItemBinder
import com.example.there.findclips.view.list.binder.ItemBinderBase
import com.example.there.findclips.view.list.item.*
import com.example.there.findclips.view.radarchart.RadarChartAxisView
import com.example.there.findclips.view.radarchart.RadarChartView
import com.example.there.findclips.view.radarchart.RadarMarkerView
import com.github.mikephil.charting.formatter.IAxisValueFormatter


class TrackFragment : BaseVMFragment<TrackViewModel>(TrackViewModel::class.java), Injectable {

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
                            album?.let { navHostFragment?.showFragment(AlbumFragment.newInstance(it), true) }
                        }
                ),
                RecyclerViewItemView(
                        RecyclerViewItemViewState(viewModel.viewState.artistsLoadingInProgress, viewModel.viewState.artists),
                        object : ListItemView<Artist>(viewModel.viewState.artists) {
                            override val itemViewBinder: ItemBinder<Artist>
                                get() = ItemBinderBase(BR.artist, R.layout.artist_item)
                        },
                        ClickHandler {
                            navHostFragment?.showFragment(ArtistFragment.newInstance(artist = it), true)
                        },
                        null,
                        null,
                        View.OnClickListener { _ ->
                            track?.let { viewModel.loadArtists(it.artists.map { artist -> artist.id }) }
                        }
                ),
                RecyclerViewItemView(
                        RecyclerViewItemViewState(viewModel.viewState.similarTracksLoadingInProgress, viewModel.viewState.similarTracks),
                        object : ListItemView<Track>(viewModel.viewState.similarTracks) {
                            override val itemViewBinder: ItemBinder<Track>
                                get() = ItemBinderBase(BR.track, R.layout.track_item)
                        },
                        ClickHandler {
                            (parentFragment as? OnTrackChangeListener)?.onTrackChanged(newTrack = it)
                        },
                        null,
                        null,
                        View.OnClickListener { _ ->

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
    ): View? = DataBindingUtil.inflate<FragmentTrackBinding>(
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
