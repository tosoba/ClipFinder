package com.example.there.findclips.track

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.base.BaseSpotifyVMFragment
import com.example.there.findclips.databinding.FragmentTrackBinding
import com.example.there.findclips.entities.Artist
import com.example.there.findclips.entities.Track
import com.example.there.findclips.lists.ArtistsList
import com.example.there.findclips.lists.SimilarTracksList
import com.example.there.findclips.trackvideos.OnTrackChangeListener
import com.example.there.findclips.util.accessToken
import com.example.there.findclips.util.app
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

    private val onArtistClickListener = object : ArtistsList.OnItemClickListener {
        override fun onClick(item: Artist) {

        }
    }

    private val onTrackChangeListener: OnTrackChangeListener?
        get() = activity as OnTrackChangeListener

    private val onSimilarTrackClickListener = object : SimilarTracksList.OnItemClickListener {
        override fun onClick(item: Track) {
            onTrackChangeListener?.onTrackChanged(newTrack = item)
        }
    }

    private val view: TrackView by lazy {
        TrackView(state = viewModel.viewState,
                artistsAdapter = ArtistsList.Adapter(viewModel.viewState.artists, R.layout.artist_item, onArtistClickListener),
                similarTracksAdapter = SimilarTracksList.Adapter(viewModel.viewState.similarTracks, R.layout.similar_track_item, onSimilarTrackClickListener))
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

    companion object {
        private const val ARG_TRACK = "ARG_TRACK"

        fun newInstanceWithTrack(track: Track): TrackFragment = TrackFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_TRACK, track)
            }
        }
    }
}
