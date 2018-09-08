package com.example.there.findclips.activities.trackvideos

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseVMFragment
import com.example.there.findclips.base.fragment.HasBackNavigation
import com.example.there.findclips.databinding.FragmentTrackVideosBinding
import com.example.there.findclips.di.Injectable
import com.example.there.findclips.fragments.search.videos.VideosSearchFragment
import com.example.there.findclips.fragments.track.TrackFragment
import com.example.there.findclips.model.entities.Track
import com.example.there.findclips.view.OnPageChangeListener
import com.example.there.findclips.view.OnTabSelectedListener
import kotlinx.android.synthetic.main.fragment_track_videos.*


class TrackVideosFragment : BaseVMFragment<TrackVideosViewModel>(), OnTrackChangeListener, Injectable, HasBackNavigation {

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

    private val onFavouriteBtnClickListener = View.OnClickListener {
        viewModel.viewState.track.get()?.let {
            viewModel.addFavouriteTrack(it)
            Toast.makeText(activity, "Added to favourites.", Toast.LENGTH_SHORT).show()
        }
    }

    private val argTrack: Track by lazy { arguments!!.getParcelable<Track>(ARG_TRACK) }

    private val view: TrackVideosView by lazy {
        TrackVideosView(state = viewModel.viewState,
                pagerAdapter = pagerAdapter,
                onPageChangeListener = onPageChangeListener,
                onTabSelectedListener = onTabSelectedListener,
                onFavouriteBtnClickListener = onFavouriteBtnClickListener
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState == null)
            viewModel.updateState(argTrack)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentTrackVideosBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_track_videos, container, false)
        return binding.apply {
            view = this@TrackVideosFragment.view
            binding.trackVideosViewpager.offscreenPageLimit = 1
        }.root
    }

    //TODO: handle onBackPressed like in ArtistFragment
//    override fun onBackPressed() {
//        if (!viewModel.onBackPressed()) {
//            super.onBackPressed()
//        } else {
//            updateCurrentFragment(viewModel.viewState.track.get()!!)
//        }
//    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(TrackVideosViewModel::class.java)
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