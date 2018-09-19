package com.example.there.findclips.fragment.track

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.databinding.library.baseAdapters.BR
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseSpotifyVMFragment
import com.example.there.findclips.databinding.FragmentTrackBinding
import com.example.there.findclips.di.Injectable
import com.example.there.findclips.fragment.album.AlbumFragment
import com.example.there.findclips.fragment.artist.ArtistFragment
import com.example.there.findclips.fragment.trackvideos.OnTrackChangeListener
import com.example.there.findclips.lifecycle.ConnectivityComponent
import com.example.there.findclips.model.entity.Artist
import com.example.there.findclips.model.entity.Track
import com.example.there.findclips.util.ext.accessToken
import com.example.there.findclips.util.ext.hostFragment
import com.example.there.findclips.util.ext.mainActivity
import com.example.there.findclips.view.list.ClickHandler
import com.example.there.findclips.view.list.binder.ItemBinder
import com.example.there.findclips.view.list.binder.ItemBinderBase
import com.example.there.findclips.view.list.item.*


class TrackFragment : BaseSpotifyVMFragment<TrackViewModel>(TrackViewModel::class.java), Injectable {

    var track: Track? = null
        set(value) {
            if (field == value || value == null) return
            field = value
            viewModel.loadDataForTrack(activity?.accessToken, value)
            viewModel.viewState.clearAll()
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        track = arguments?.getParcelable(ARG_TRACK)
    }

    private val trackAdapter: TrackAdapter by lazy {
        TrackAdapter(
                AlbumInfoItemView(
                        AlbumInfoViewState(viewModel.viewState.albumLoadingInProgress, viewModel.viewState.album),
                        View.OnClickListener { _ ->
                            val album = viewModel.viewState.album.get()
                            album?.let { hostFragment?.showFragment(AlbumFragment.newInstance(it), true) }
                        }
                ),
                RecyclerViewItemView(
                        RecyclerViewItemViewState(viewModel.viewState.artistsLoadingInProgress, viewModel.viewState.artists),
                        object : ListItemView<Artist>(viewModel.viewState.artists) {
                            override val itemViewBinder: ItemBinder<Artist>
                                get() = ItemBinderBase(BR.artist, R.layout.artist_item)
                        },
                        ClickHandler {
                            hostFragment?.showFragment(ArtistFragment.newInstance(artist = it), true)
                        },
                        null,
                        null
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
                        null
                )
        )
    }

    private val view: TrackView by lazy {
        TrackView(viewModel.viewState, trackAdapter)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentTrackBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_track, container, false)
        binding.view = view
        binding.trackRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return binding.root
    }

    private val connectivityComponent: ConnectivityComponent by lazy {
        ConnectivityComponent(
                activity!!,
                {
                    viewModel.viewState.album.get() != null &&
                            viewModel.viewState.artists.isNotEmpty() &&
                            viewModel.viewState.similarTracks.isNotEmpty()
                },
                mainActivity!!.connectivitySnackbarParentView!!,
                ::loadData
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(connectivityComponent)
    }

    private fun loadData() {
        track?.let { viewModel.loadDataForTrack(activity?.accessToken, it) }
    }

    companion object {
        private const val ARG_TRACK = "ARG_TRACK"

        fun newInstanceWithTrack(track: Track): TrackFragment = TrackFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_TRACK, track)
            }
        }
    }
}
