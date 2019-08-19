package com.example.soundcloudtrackvideos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.airbnb.mvrx.*
import com.example.coreandroid.base.fragment.BaseListFragment
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


class SoundCloudTrackVideosFragment : BaseMvRxFragment(),
        OnTrackChangeListener<SoundCloudTrack>,
        GoesToPreviousStateOnBackPressed {

    private val viewModel: SoundCloudTrackVideosViewModel by fragmentViewModel()

    private val argTrack: SoundCloudTrack by args()

    private val onPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            sound_cloud_track_videos_tab_layout?.getTabAt(position)?.select()
            withCurrentTrack { updateCurrentFragment(it) }
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

    private val view: SoundCloudTrackVideosViewBinding by lazy {
        SoundCloudTrackVideosViewBinding(
                track = MutableLiveData<SoundCloudTrack>().apply { value = argTrack },
                pagerAdapter = pagerAdapter,
                onPageChangeListener = onPageChangeListener,
                onTabSelectedListener = onTabSelectedListener,
                onFavouriteBtnClickListener = View.OnClickListener { }
        )
    }

    private val disposablesComponent = DisposablesComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadSimilarTracks(argTrack.id) //TODO: move this to SoundCloudTrackFragment
        lifecycle.addObserver(disposablesComponent)
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

    override fun onTrackChanged(newTrack: SoundCloudTrack) = viewModel.updateTrack(newTrack)

    override fun invalidate() = Unit

    private fun updateCurrentFragment(newTrack: SoundCloudTrack) {
        when (val currentFragment = pagerAdapter.currentFragment) {
            is VideosSearchFragment -> currentFragment.query = newTrack.title
            is SoundCloudTracksFragment -> viewModel.loadSimilarTracks(newTrack.id)
        }
    }

    private fun withCurrentTrack(block: (SoundCloudTrack) -> Unit) = withState(viewModel) { state ->
        state.tracks.value.lastOrNull()?.let(block)
    }

    companion object {
        fun newInstance(track: SoundCloudTrack) = SoundCloudTrackVideosFragment().apply {
            arguments = Bundle().apply { putParcelable(MvRx.KEY_ARG, track) }
        }
    }
}
