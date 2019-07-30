package com.example.spotifytrackvideos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.coreandroid.base.fragment.BaseVMFragment
import com.example.coreandroid.base.fragment.GoesToPreviousStateOnBackPressed
import com.example.coreandroid.base.handler.OnTrackChangeListener
import com.example.coreandroid.lifecycle.DisposablesComponent
import com.example.coreandroid.lifecycle.OnPropertyChangedCallbackComponent
import com.example.coreandroid.model.spotify.Track
import com.example.coreandroid.util.ext.*
import com.example.coreandroid.view.OnPageChangeListener
import com.example.coreandroid.view.OnTabSelectedListener
import com.example.spotifytrack.TrackFragment
import com.example.spotifytrackvideos.databinding.FragmentTrackVideosBinding
import com.example.youtubesearch.VideosSearchFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_track_videos.*

class TrackVideosFragment :
        BaseVMFragment<TrackVideosViewModel>(TrackVideosViewModel::class),
        OnTrackChangeListener<Track>,
        GoesToPreviousStateOnBackPressed {

    private val onPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            track_videos_tab_layout?.getTabAt(position)?.select()
            updateCurrentFragment(viewModel.viewState.track.get()!!)
        }
    }

    private val onTabSelectedListener = object : OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.let { track_videos_viewpager?.currentItem = it.position }
        }
    }

    private val pagerAdapter: TrackVideosPagerAdapter by lazy {
        TrackVideosPagerAdapter(
                manager = childFragmentManager,
                fragments = arrayOf(
                        VideosSearchFragment.newInstanceWithQuery(argTrack.query),
                        TrackFragment.newInstanceWithTrack(argTrack)
                )
        )
    }

    private val onFavouriteBtnClickListener = View.OnClickListener { _ ->
        viewModel.viewState.track.get()?.let {
            if (viewModel.viewState.isSavedAsFavourite.get() == true) {
                viewModel.deleteFavouriteTrack(it)
                Toast.makeText(activity, "${it.name} removed from favourite tracks.", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.addFavouriteTrack(it)
                Toast.makeText(activity, "${it.name} added to favourite tracks.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val argTrack: Track by lazy { arguments!!.getParcelable<Track>(ARG_TRACK) }

    private val view: TrackVideosView by lazy {
        TrackVideosView(
                state = viewModel.viewState,
                pagerAdapter = pagerAdapter,
                onPageChangeListener = onPageChangeListener,
                onTabSelectedListener = onTabSelectedListener,
                onFavouriteBtnClickListener = onFavouriteBtnClickListener
        )
    }

    private val disposablesComponent = DisposablesComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        lifecycle.addObserver(disposablesComponent)
        viewModel.updateState(argTrack)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentTrackVideosBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_track_videos, container, false)
        lifecycle.addObserver(OnPropertyChangedCallbackComponent(viewModel.viewState.isSavedAsFavourite) { _, _ ->
            binding.trackFavouriteFab.hideAndShow()
        })
        enableSpotifyPlayButton {
            viewModel.viewState.track.get()?.let { loadTrack(track = it) }
        }
        return binding.apply {
            view = this@TrackVideosFragment.view
            trackVideosToolbarGradientBackgroundView.loadBackgroundGradient(argTrack.iconUrl, disposablesComponent)
            trackVideosViewpager.offscreenPageLimit = 1
            trackVideosToolbar.setupWithBackNavigation(appCompatActivity, ::onBackPressed)
        }.root
    }

    override fun onBackPressed() {
        if (!viewModel.onBackPressed()) backPressedWithNoPreviousStateController?.onBackPressedWithNoPreviousState()
        else viewModel.viewState.track.get()?.let {
            updateCurrentFragment(it)
            track_videos_toolbar_gradient_background_view?.loadBackgroundGradient(it.iconUrl, disposablesComponent)
        }
    }

    override fun onTrackChanged(newTrack: Track) = with(newTrack) {
        viewModel.updateState(this)
        track_videos_toolbar_gradient_background_view?.loadBackgroundGradient(iconUrl, disposablesComponent)
        updateCurrentFragment(this)
    }

    private fun updateCurrentFragment(newTrack: Track) {
        when (val currentFragment = pagerAdapter.currentFragment) {
            is VideosSearchFragment -> currentFragment.query = newTrack.query
            is TrackFragment -> currentFragment.track = newTrack
        }
    }

    companion object {
        private const val ARG_TRACK = "ARG_TRACK"

        fun newInstance(track: Track) = TrackVideosFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_TRACK, track)
            }
        }
    }
}
