package com.example.there.findclips.fragment.track

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
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
import com.example.there.findclips.lifecycle.DisposablesComponent
import com.example.there.findclips.model.entity.Track
import com.example.there.findclips.util.ext.accessToken
import com.example.there.findclips.util.ext.hostFragment
import com.example.there.findclips.util.ext.mainActivity
import com.example.there.findclips.view.list.impl.ArtistsList
import com.example.there.findclips.view.list.impl.TracksList
import com.example.there.findclips.view.list.item.AlbumInfoItemView
import com.example.there.findclips.view.list.item.AlbumInfoViewState


class TrackFragment : BaseSpotifyVMFragment<TrackViewModel>(), Injectable {

    var track: Track? = null
        set(value) {
            if (field == value || value == null) return
            field = value
            viewModel.loadDataForTrack(activity?.accessToken, value)
            viewModel.viewState.clearAll()
        }

    private val disposablesComponent = DisposablesComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(disposablesComponent)
        initItemClicks()

        track = arguments?.getParcelable(ARG_TRACK)
    }

    private fun initItemClicks() = disposablesComponent.addAll(
            artistsAdapter.itemClicked.subscribe {
                hostFragment?.showFragment(ArtistFragment.newInstance(artist = it), true)
            },
            similarTracksAdapter.itemClicked.subscribe {
                (parentFragment as? OnTrackChangeListener)?.onTrackChanged(newTrack = it)
            }
    )


    private val artistsAdapter: ArtistsList.Adapter by lazy {
        ArtistsList.Adapter(viewModel.viewState.artists, R.layout.artist_item)
    }

    private val similarTracksAdapter: TracksList.Adapter by lazy {
        TracksList.Adapter(viewModel.viewState.similarTracks, R.layout.track_item)
    }

    private val trackAdapter: TrackAdapter by lazy {
        TrackAdapter(AlbumInfoItemView(AlbumInfoViewState(viewModel.viewState.albumLoadingInProgress, viewModel.viewState.album), View.OnClickListener { _ ->
            val album = viewModel.viewState.album.get()
            album?.let { hostFragment?.showFragment(AlbumFragment.newInstance(it), true) }
        }), artistsAdapter, similarTracksAdapter, viewModel.viewState.artistsLoadingInProgress, viewModel.viewState.similarTracksLoadingInProgress)
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
