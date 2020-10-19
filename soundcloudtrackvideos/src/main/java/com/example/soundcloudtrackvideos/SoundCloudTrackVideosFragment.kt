package com.example.soundcloudtrackvideos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.args
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.example.core.android.base.fragment.GoesToPreviousStateOnBackPressed
import com.example.core.android.base.handler.SoundCloudPlayerController
import com.example.core.android.base.trackvideos.TrackVideosViewBinding
import com.example.core.android.base.trackvideos.TrackVideosViewState
import com.example.core.android.model.soundcloud.SoundCloudTrack
import com.example.core.android.util.ext.*
import com.example.core.android.view.OnPageChangeListener
import com.example.core.android.view.OnTabSelectedListener
import com.example.core.android.view.viewpager.adapter.CustomCurrentStatePagerAdapter
import com.example.core.ext.castAs
import com.example.soundcloudtrackvideos.databinding.FragmentSoundCloudTrackVideosBinding
import com.example.soundcloudtrackvideos.track.SoundCloudTrackFragment
import com.example.youtubesearch.VideosSearchFragment
import com.google.android.material.tabs.TabLayout
import com.wada811.lifecycledispose.disposeOnDestroy
import kotlinx.android.synthetic.main.fragment_sound_cloud_track_videos.*

class SoundCloudTrackVideosFragment : BaseMvRxFragment(), GoesToPreviousStateOnBackPressed {

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
                SoundCloudTrackFragment.new(argTrack)
            )
        )
    }

    private val view: TrackVideosViewBinding<SoundCloudTrack> by lazy {
        TrackVideosViewBinding(
            fragmentTabs = arrayOf("Clips", "Similar"),
            track = MutableLiveData<SoundCloudTrack>().apply { value = argTrack },
            pagerAdapter = pagerAdapter,
            onPageChangeListener = onPageChangeListener,
            onTabSelectedListener = onTabSelectedListener,
            onFavouriteBtnClickListener = View.OnClickListener { }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSoundCloudTrackVideosBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_sound_cloud_track_videos,
            container,
            false
        )

        viewModel.selectSubscribe(this, TrackVideosViewState<SoundCloudTrack>::tracks) { tracks ->
            tracks.value.lastOrNull()?.let { track ->
                view.track.value = track
                track.artworkUrl?.let {
                    binding.soundCloudTrackVideosToolbarGradientBackgroundView
                        .loadBackgroundGradient(it)
                        .disposeOnDestroy(this)
                }
                binding.executePendingBindings()
                updateCurrentFragment(track)
            } ?: backPressedWithNoPreviousStateController?.onBackPressedWithNoPreviousState()
        }

        mainContentFragment?.enablePlayButton {
            withCurrentTrack { activity?.castAs<SoundCloudPlayerController>()?.loadTrack(it) }
        }

        return binding.apply {
            lifecycleOwner = this@SoundCloudTrackVideosFragment
            view = this@SoundCloudTrackVideosFragment.view
            soundCloudTrackVideosViewpager.offscreenPageLimit = 1
            soundCloudTrackVideosToolbar.setupWithBackNavigation(
                requireActivity() as? AppCompatActivity,
                ::onBackPressed
            )
        }.root
    }

    override fun onBackPressed() = viewModel.onBackPressed()

    override fun invalidate() = Unit

    private fun updateCurrentFragment(newTrack: SoundCloudTrack) {
        when (val currentFragment = pagerAdapter.currentFragment) {
            is VideosSearchFragment -> currentFragment.onNewQuery(newTrack.title)
            is SoundCloudTrackFragment -> currentFragment.track = newTrack
        }
    }

    private fun withCurrentTrack(block: (SoundCloudTrack) -> Unit) = withState(viewModel) { state ->
        state.tracks.value.lastOrNull()?.let(block)
    }

    companion object {
        fun new(track: SoundCloudTrack): SoundCloudTrackVideosFragment = newFragmentWithMvRxArg(track)
    }
}
