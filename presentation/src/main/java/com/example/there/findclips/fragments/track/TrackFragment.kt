package com.example.there.findclips.fragments.track

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.Router
import com.example.there.findclips.activities.trackvideos.OnTrackChangeListener
import com.example.there.findclips.base.BaseSpotifyVMFragment
import com.example.there.findclips.databinding.FragmentTrackBinding
import com.example.there.findclips.model.entities.Artist
import com.example.there.findclips.model.entities.Track
import com.example.there.findclips.util.accessToken
import com.example.there.findclips.util.app
import com.example.there.findclips.view.lists.ArtistsList
import com.example.there.findclips.view.lists.OnArtistClickListener
import com.example.there.findclips.view.lists.OnTrackClickListener
import com.example.there.findclips.view.lists.TracksList
import javax.inject.Inject


class TrackFragment : BaseSpotifyVMFragment<TrackViewModel>() {

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
        override fun onClick(item: Artist) = Router.goToArtistActivity(activity, artist = item)
    }

    private val onTrackChangeListener: OnTrackChangeListener?
        get() = activity as OnTrackChangeListener

    private val onTrackClickListener = object : OnTrackClickListener {
        override fun onClick(item: Track) {
            onTrackChangeListener?.onTrackChanged(newTrack = item)
        }
    }

    private val view: TrackView by lazy {
        TrackView(state = viewModel.viewState,
                artistsAdapter = ArtistsList.Adapter(viewModel.viewState.artists, R.layout.artist_item, onArtistClickListener),
                similarTracksAdapter = TracksList.Adapter(viewModel.viewState.similarTracks, R.layout.track_item, onTrackClickListener),
                onAlbumImageViewClickListener = View.OnClickListener {
                    val album = viewModel.viewState.album.get()
                    album?.let { Router.goToAlbumAcitivity(activity, album = it) }
                })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentTrackBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_track, container, false)
        binding.view = view
        binding.trackArtistsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.trackSimilarTracksRecyclerView.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
        return binding.root
    }

    override fun initComponent() {
        activity?.app?.createTrackSubComponent()?.inject(this)
    }

    override fun releaseComponent() {
        activity?.app?.releaseTrackSubComponent()
    }

    @Inject
    lateinit var factory: TrackVMFactory

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(TrackViewModel::class.java)
    }

    override fun isDataLoaded(): Boolean = viewModel.viewState.album.get() != null &&
            viewModel.viewState.artists.isNotEmpty() &&
            viewModel.viewState.similarTracks.isNotEmpty()

    override fun reloadData() {
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
