package com.example.there.findclips.fragment.trackvideos

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseVMFragment
import com.example.there.findclips.base.fragment.GoesToPreviousStateOnBackPressed
import com.example.there.findclips.databinding.FragmentTrackVideosBinding
import com.example.there.findclips.di.Injectable
import com.example.there.findclips.fragment.search.videos.VideosSearchFragment
import com.example.there.findclips.fragment.spotifyitem.track.TrackFragment
import com.example.there.findclips.lifecycle.OnPropertyChangedCallbackComponent
import com.example.there.findclips.model.entity.Track
import com.example.there.findclips.util.ext.hideAndShow
import com.example.there.findclips.util.ext.mainActivity
import com.example.there.findclips.util.ext.setupWithBackNavigation
import com.example.there.findclips.view.OnPageChangeListener
import com.example.there.findclips.view.OnTabSelectedListener
import kotlinx.android.synthetic.main.fragment_track_videos.*


class TrackVideosFragment :
        BaseVMFragment<TrackVideosViewModel>(TrackVideosViewModel::class.java),
        OnTrackChangeListener,
        Injectable,
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

    override fun onTrackChanged(newTrack: Track) {
        viewModel.updateState(newTrack)
        updateCurrentFragment(newTrack)
    }

    private fun updateCurrentFragment(newTrack: Track) {
        val currentFragment = pagerAdapter.currentFragment
        when (currentFragment) {
            is VideosSearchFragment -> currentFragment.query = newTrack.query
            is TrackFragment -> currentFragment.track = newTrack
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

    private val onPlayBtnClickListener = View.OnClickListener { _ ->
        viewModel.viewState.track.get()?.let {
            val playTrack: () -> Unit = { mainActivity?.loadTrack(track = it) }
            if (mainActivity?.playerLoggedIn == true) {
                playTrack()
            } else {
                mainActivity?.showLoginDialog()
                mainActivity?.onLoginSuccessful = playTrack
            }
        }
    }

    private val argTrack: Track by lazy { arguments!!.getParcelable<Track>(ARG_TRACK) }

    private val view: TrackVideosView by lazy {
        TrackVideosView(state = viewModel.viewState,
                pagerAdapter = pagerAdapter,
                onPageChangeListener = onPageChangeListener,
                onTabSelectedListener = onTabSelectedListener,
                onFavouriteBtnClickListener = onFavouriteBtnClickListener,
                onPlayBtnClickListener = onPlayBtnClickListener
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel.updateState(argTrack)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentTrackVideosBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_track_videos, container, false)
        lifecycle.addObserver(OnPropertyChangedCallbackComponent(viewModel.viewState.isSavedAsFavourite) { _, _ ->
            binding.trackFavouriteFab.hideAndShow()
        })
        return binding.apply {
            view = this@TrackVideosFragment.view
            trackVideosViewpager.offscreenPageLimit = 1
            trackVideosToolbar.setupWithBackNavigation(mainActivity)
        }.root
    }

    override fun onBackPressed() {
        if (!viewModel.onBackPressed()) {
            mainActivity?.backPressedOnNoPreviousFragmentState()
        } else {
            updateCurrentFragment(viewModel.viewState.track.get()!!)
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
