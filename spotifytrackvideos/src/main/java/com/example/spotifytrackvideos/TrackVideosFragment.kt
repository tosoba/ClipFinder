package com.example.spotifytrackvideos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.airbnb.mvrx.*
import com.example.coreandroid.base.fragment.GoesToPreviousStateOnBackPressed
import com.example.coreandroid.base.trackvideos.TrackVideosViewBinding
import com.example.coreandroid.base.trackvideos.TrackVideosViewState
import com.example.coreandroid.model.spotify.Track
import com.example.coreandroid.util.ext.*
import com.example.coreandroid.view.OnPageChangeListener
import com.example.coreandroid.view.OnTabSelectedListener
import com.example.coreandroid.view.viewpager.adapter.CustomCurrentStatePagerAdapter
import com.example.spotifytrackvideos.databinding.FragmentTrackVideosBinding
import com.example.spotifytrackvideos.track.TrackFragment
import com.example.youtubesearch.VideosSearchFragment
import com.google.android.material.tabs.TabLayout
import com.wada811.lifecycledispose.disposeOnDestroy
import kotlinx.android.synthetic.main.fragment_track_videos.*

class TrackVideosFragment : BaseMvRxFragment(), GoesToPreviousStateOnBackPressed {

    private val viewModel: TrackVideosViewModel by fragmentViewModel()

    private val onPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            track_videos_tab_layout?.getTabAt(position)?.select()
            withCurrentTrack { updateCurrentFragment(it) }
        }
    }

    private val onTabSelectedListener = object : OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.let { track_videos_viewpager?.currentItem = it.position }
        }
    }

    private val pagerAdapter: CustomCurrentStatePagerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        CustomCurrentStatePagerAdapter(
            fragmentManager = childFragmentManager,
            fragments = arrayOf(
                VideosSearchFragment.newInstanceWithQuery(argTrack.query),
                TrackFragment.newInstanceWithTrack(argTrack)
            )
        )
    }

    private val onFavouriteBtnClickListener = View.OnClickListener { _ ->
        viewModel.toggleTrackFavouriteState()
        //TODO: add a callback that will show a toast with a msg saying: added/deleted from favs
    }

    private val argTrack: Track by args()

    private val view: TrackVideosViewBinding<Track> by lazy {
        TrackVideosViewBinding(
            fragmentTabs = arrayOf("Clips", "Info"),
            track = MutableLiveData<Track>().apply { value = argTrack },
            pagerAdapter = pagerAdapter,
            onPageChangeListener = onPageChangeListener,
            onTabSelectedListener = onTabSelectedListener,
            onFavouriteBtnClickListener = onFavouriteBtnClickListener
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentTrackVideosBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_track_videos, container, false)

        viewModel.selectSubscribe(this, TrackVideosViewState<Track>::isSavedAsFavourite) {
            binding.trackFavouriteFab.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                if (it.value) R.drawable.delete else R.drawable.favourite))
            binding.trackFavouriteFab.hideAndShow()
        }

        viewModel.selectSubscribe(this, TrackVideosViewState<Track>::tracks) { tracks ->
            tracks.value.lastOrNull()?.let {
                view.track.value = it
                binding.trackVideosToolbarGradientBackgroundView
                    .loadBackgroundGradient(it.iconUrl)
                    .disposeOnDestroy(this)
                binding.executePendingBindings()
                updateCurrentFragment(it)
            } ?: backPressedWithNoPreviousStateController?.onBackPressedWithNoPreviousState()
        }

        enableSpotifyPlayButton { withCurrentTrack(::loadTrack) }

        return binding.apply {
            lifecycleOwner = this@TrackVideosFragment
            view = this@TrackVideosFragment.view
            trackVideosViewpager.offscreenPageLimit = 1
            trackVideosToolbar.setupWithBackNavigation(
                requireActivity() as? AppCompatActivity,
                ::onBackPressed
            )
        }.root
    }

    override fun onBackPressed() = viewModel.onBackPressed()

    override fun invalidate() = Unit

    private fun updateCurrentFragment(newTrack: Track) {
        when (val currentFragment = pagerAdapter.currentFragment) {
            is VideosSearchFragment -> currentFragment.query = newTrack.query
            is TrackFragment -> currentFragment.track = newTrack
        }
    }

    private fun withCurrentTrack(block: (Track) -> Unit) = withState(viewModel) { state ->
        state.tracks.value.lastOrNull()?.let(block)
    }

    companion object {
        fun newInstance(track: Track) = TrackVideosFragment().apply {
            arguments = Bundle().apply { putParcelable(MvRx.KEY_ARG, track) }
        }
    }
}
