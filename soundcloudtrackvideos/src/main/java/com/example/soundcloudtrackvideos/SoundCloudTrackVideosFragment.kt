package com.example.soundcloudtrackvideos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.coreandroid.base.fragment.BaseListFragment
import com.example.coreandroid.base.fragment.BaseVMFragment
import com.example.coreandroid.base.fragment.GoesToPreviousStateOnBackPressed
import com.example.coreandroid.base.handler.OnTrackChangeListener
import com.example.coreandroid.lifecycle.DisposablesComponent
import com.example.coreandroid.lifecycle.OnPropertyChangedCallbackComponent
import com.example.coreandroid.model.soundcloud.SoundCloudTrack
import com.example.coreandroid.util.ext.*
import com.example.coreandroid.view.OnPageChangeListener
import com.example.coreandroid.view.OnTabSelectedListener
import com.example.coreandroid.view.viewpager.adapter.CustomCurrentStatePagerAdapter
import com.example.itemlist.soundcloud.SoundCloudTracksFragment
import com.example.soundcloudtrackvideos.databinding.FragmentSoundCloudTrackVideosBinding
import com.example.youtubesearch.VideosSearchFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_sound_cloud_track_videos.*


class SoundCloudTrackVideosFragment :
        BaseVMFragment<SoundCloudTrackVideosViewModel>(SoundCloudTrackVideosViewModel::class),
        OnTrackChangeListener<SoundCloudTrack>,
        GoesToPreviousStateOnBackPressed {

    private val argTrack: SoundCloudTrack by lazy { arguments!!.getParcelable<SoundCloudTrack>(ARG_TRACK) }

    private val onPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            sound_cloud_track_videos_tab_layout?.getTabAt(position)?.select()
            updateCurrentFragment(viewModel.viewState.track.get()!!)
        }
    }

    private val onTabSelectedListener = object : OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.let { sound_cloud_track_videos_viewpager?.currentItem = it.position }
        }
    }

    private val pagerAdapter: CustomCurrentStatePagerAdapter by lazy {
        CustomCurrentStatePagerAdapter(
                fragmentManager = childFragmentManager,
                fragments = arrayOf(
                        VideosSearchFragment.newInstanceWithQuery(argTrack.title),
                        BaseListFragment.newInstance<SoundCloudTracksFragment, SoundCloudTrack>(
                                "",
                                "",
                                items = null
                        ).apply {
                            refreshData = { fragment ->
                                viewModel.similarTracks.value?.let {
                                    fragment.updateItems(it)
                                }
                            }

                            onItemClick = {
                                (parentFragment as? OnTrackChangeListener<SoundCloudTrack>)?.onTrackChanged(newTrack = it)
                            }
                        }
                )
        )
    }

    private val view: SoundCloudTrackVideosView by lazy {
        SoundCloudTrackVideosView(
                state = viewModel.viewState,
                pagerAdapter = pagerAdapter,
                onPageChangeListener = onPageChangeListener,
                onTabSelectedListener = onTabSelectedListener,
                onFavouriteBtnClickListener = View.OnClickListener { }
        )
    }

    private val disposablesComponent = DisposablesComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadSimilarTracks(argTrack.id)
        lifecycle.addObserver(disposablesComponent)
        viewModel.updateState(argTrack)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentSoundCloudTrackVideosBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sound_cloud_track_videos, container, false)
        lifecycle.addObserver(OnPropertyChangedCallbackComponent(viewModel.viewState.isSavedAsFavourite) { _, _ ->
            binding.soundCloudTrackFavouriteFab.hideAndShow()
        })
        mainContentFragment?.enablePlayButton {
            viewModel.viewState.track.get()?.let {
                soundCloudPlayerController?.loadTrack(it)
            }
        }
        return binding.apply {
            view = this@SoundCloudTrackVideosFragment.view
            argTrack.artworkUrl?.let {
                soundCloudTrackVideosToolbarGradientBackgroundView.loadBackgroundGradient(it, disposablesComponent)
            }
            soundCloudTrackVideosViewpager.offscreenPageLimit = 1
            soundCloudTrackVideosToolbar.setupWithBackNavigation(appCompatActivity, ::onBackPressed)
        }.root
    }

    override fun onBackPressed() {
        if (!viewModel.onBackPressed()) {
            backPressedWithNoPreviousStateController?.onBackPressedWithNoPreviousState()
        } else {
            updateCurrentFragment(viewModel.viewState.track.get()!!)
            argTrack.artworkUrl?.let {
                sound_cloud_track_videos_toolbar_gradient_background_view?.loadBackgroundGradient(it, disposablesComponent)
            }
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.similarTracks.observeIgnoringNulls(this) {
            val currentFragment = pagerAdapter.currentFragment
            if (currentFragment is SoundCloudTracksFragment) {
                currentFragment.updateItems(it)
            }
        }
    }

    override fun onTrackChanged(newTrack: SoundCloudTrack) = with(newTrack) {
        viewModel.updateState(this)
        artworkUrl?.let {
            sound_cloud_track_videos_toolbar_gradient_background_view?.loadBackgroundGradient(it, disposablesComponent)
        }
        updateCurrentFragment(this)
    }

    private fun updateCurrentFragment(newTrack: SoundCloudTrack) {
        when (val currentFragment = pagerAdapter.currentFragment) {
            is VideosSearchFragment -> currentFragment.query = newTrack.title
            is SoundCloudTracksFragment -> viewModel.loadSimilarTracks(newTrack.id)
        }
    }

    companion object {
        private const val ARG_TRACK = "ARG_TRACK"

        fun newInstance(track: SoundCloudTrack) = SoundCloudTrackVideosFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_TRACK, track)
            }
        }
    }
}
