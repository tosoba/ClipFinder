package com.example.there.findclips.fragment.track

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.there.findclips.view.list.ArtistsList
import com.example.there.findclips.view.list.OnArtistClickListener
import com.example.there.findclips.view.list.OnTrackClickListener
import com.example.there.findclips.view.list.TracksList
import kotlinx.android.synthetic.main.fragment_track.*


class TrackFragment : BaseSpotifyVMFragment<TrackViewModel>(), Injectable {

    var track: Track? = null
        set(value) {
            if (field == value || value == null) return
            field = value
            viewModel.loadDataForTrack(activity?.accessToken, value)
            viewModel.viewState.clearAll()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            track = arguments?.getParcelable(ARG_TRACK)
        }
    }

    private val onArtistClickListener = object : OnArtistClickListener {
        override fun onClick(item: Artist) {
            hostFragment?.showFragment(ArtistFragment.newInstance(artist = item), true)
        }
    }

    private val onTrackClickListener = object : OnTrackClickListener {
        override fun onClick(item: Track) {
            (parentFragment as? OnTrackChangeListener)?.onTrackChanged(newTrack = item)
        }
    }

    private val view: TrackView by lazy {
        TrackView(state = viewModel.viewState,
                artistsAdapter = ArtistsList.Adapter(viewModel.viewState.artists, R.layout.artist_item, onArtistClickListener),
                similarTracksAdapter = TracksList.Adapter(viewModel.viewState.similarTracks, R.layout.track_item, onTrackClickListener),
                onAlbumImageViewClickListener = View.OnClickListener { _ ->
                    val album = viewModel.viewState.album.get()
                    album?.let { hostFragment?.showFragment(AlbumFragment.newInstance(it), true) }
                })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentTrackBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_track, container, false)
        binding.view = view
        binding.trackArtistsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.trackSimilarTracksRecyclerView.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
        return binding.root
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(TrackViewModel::class.java)
    }

    private val connectivityComponent: ConnectivityComponent by lazy {
        ConnectivityComponent(
                activity!!,
                {
                    viewModel.viewState.album.get() != null &&
                            viewModel.viewState.artists.isNotEmpty() &&
                            viewModel.viewState.similarTracks.isNotEmpty()
                },
                track_root_layout,
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
