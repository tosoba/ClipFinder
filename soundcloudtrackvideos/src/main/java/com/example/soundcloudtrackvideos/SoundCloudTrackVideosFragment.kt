package com.example.soundcloudtrackvideos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.airbnb.mvrx.*
import com.example.coreandroid.base.fragment.GoesToPreviousStateOnBackPressed
import com.example.coreandroid.base.trackvideos.TrackVideosViewBinding
import com.example.coreandroid.base.trackvideos.TrackVideosViewState
import com.example.coreandroid.lifecycle.DisposablesComponent
import com.example.coreandroid.model.soundcloud.SoundCloudTrack
import com.example.coreandroid.util.ext.*
import com.example.coreandroid.view.OnPageChangeListener
import com.example.coreandroid.view.OnTabSelectedListener
import com.example.coreandroid.view.viewpager.adapter.CustomCurrentStatePagerAdapter
import com.example.soundcloudtrackvideos.databinding.FragmentSoundCloudTrackVideosBinding
import com.example.soundcloudtrackvideos.track.SoundCloudTrackFragment
import com.example.youtubesearch.VideosSearchFragment
import com.google.android.material.tabs.TabLayout
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
                        SoundCloudTrackFragment.newInstance(argTrack)
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

    private val disposablesComponent = DisposablesComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(disposablesComponent)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentSoundCloudTrackVideosBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sound_cloud_track_videos, container, false)

        viewModel.selectSubscribe(this, TrackVideosViewState<SoundCloudTrack>::isSavedAsFavourite) {
            binding.soundCloudTrackFavouriteFab.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                    if (it.value) R.drawable.delete else R.drawable.favourite))
            binding.soundCloudTrackFavouriteFab.hideAndShow()
        }

        viewModel.selectSubscribe(this, TrackVideosViewState<SoundCloudTrack>::tracks) { tracks ->
            tracks.value.lastOrNull()?.let { track ->
                view.track.value = track
                track.artworkUrl?.let {
                    binding.soundCloudTrackVideosToolbarGradientBackgroundView.loadBackgroundGradient(it, disposablesComponent)
                }
                binding.executePendingBindings()
                updateCurrentFragment(track)
            } ?: backPressedWithNoPreviousStateController?.onBackPressedWithNoPreviousState()
        }

        mainContentFragment?.enablePlayButton {
            withCurrentTrack { soundCloudPlayerController?.loadTrack(it) }
        }

        return binding.apply {
            lifecycleOwner = this@SoundCloudTrackVideosFragment
            view = this@SoundCloudTrackVideosFragment.view
            soundCloudTrackVideosViewpager.offscreenPageLimit = 1
            soundCloudTrackVideosToolbar.setupWithBackNavigation(appCompatActivity, ::onBackPressed)
        }.root
    }

    override fun onBackPressed() = viewModel.onBackPressed()

    override fun invalidate() = Unit

    private fun updateCurrentFragment(newTrack: SoundCloudTrack) {
        when (val currentFragment = pagerAdapter.currentFragment) {
            is VideosSearchFragment -> currentFragment.query = newTrack.title
            is SoundCloudTrackFragment -> currentFragment.track = newTrack
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
