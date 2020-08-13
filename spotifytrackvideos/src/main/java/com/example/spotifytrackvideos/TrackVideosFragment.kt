package com.example.spotifytrackvideos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.airbnb.mvrx.*
import com.example.core.android.base.fragment.GoesToPreviousStateOnBackPressed
import com.example.core.android.base.trackvideos.TrackVideosViewState
import com.example.core.android.model.spotify.Track
import com.example.core.android.util.ext.*
import com.example.core.android.view.OnPageChangeListener
import com.example.core.android.view.viewpager.adapter.TitledCustomCurrentStatePagerAdapter
import com.example.spotifytrackvideos.databinding.FragmentTrackVideosBinding
import com.example.spotifytrackvideos.track.TrackFragment
import com.example.youtubesearch.VideosSearchFragment
import com.wada811.lifecycledispose.disposeOnDestroy

class TrackVideosFragment : BaseMvRxFragment(), GoesToPreviousStateOnBackPressed {

    private val viewModel: TrackVideosViewModel by fragmentViewModel()

    private val argTrack: Track by args()

    private val pagerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        TitledCustomCurrentStatePagerAdapter(
            fragmentManager = childFragmentManager,
            titledFragments = arrayOf(
                getString(R.string.clips) to VideosSearchFragment.newInstanceWithQuery(argTrack.query),
                getString(R.string.info) to TrackFragment.newInstanceWithTrack(argTrack)
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTrackVideosBinding.inflate(inflater, container, false)

        viewModel.selectSubscribe(this, TrackVideosViewState<Track>::isSavedAsFavourite) {
            binding.trackFavouriteFab.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    if (it.value) R.drawable.delete else R.drawable.favourite
                )
            )
            binding.trackFavouriteFab.hideAndShow()
        }

        val currentTrack = MutableLiveData<Track>(argTrack)
        viewModel.selectSubscribe(this, TrackVideosViewState<Track>::tracks) { tracks ->
            tracks.value.lastOrNull()?.let {
                currentTrack.value = it
                binding.trackVideosToolbarGradientBackgroundView
                    .loadBackgroundGradient(it.iconUrl)
                    .disposeOnDestroy(this)
                binding.executePendingBindings()
                updateCurrentFragment(it)
            } ?: backPressedWithNoPreviousStateController?.onBackPressedWithNoPreviousState()
        }

        fun withCurrentTrack(block: (Track) -> Unit) = withState(viewModel) { state ->
            state.tracks.value.lastOrNull()?.let(block)
        }

        enableSpotifyPlayButton { withCurrentTrack(::loadTrack) }

        return binding.apply {
            lifecycleOwner = this@TrackVideosFragment
            track = currentTrack
            trackVideosViewpager.adapter = pagerAdapter
            trackVideosViewpager.offscreenPageLimit = pagerAdapter.count - 1
            trackVideosViewpager.addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageSelected(position: Int) {
                    trackVideosTabLayout.getTabAt(position)?.select()
                    withCurrentTrack { updateCurrentFragment(it) }
                }
            })
            trackVideosTabLayout.setupWithViewPager(trackVideosViewpager)
            trackVideosToolbar.setupWithBackNavigation(
                requireActivity() as? AppCompatActivity,
                ::onBackPressed
            )
            trackFavouriteFab.setOnClickListener { viewModel.toggleTrackFavouriteState() }
        }.root
    }

    override fun onBackPressed() = viewModel.onBackPressed()

    override fun onOptionsItemSelected(item: MenuItem): Boolean = false

    override fun invalidate() = Unit

    private fun updateCurrentFragment(newTrack: Track) {
        when (val currentFragment = pagerAdapter.currentFragment) {
            is VideosSearchFragment -> currentFragment.query = newTrack.query
            is TrackFragment -> currentFragment.track = newTrack
        }
    }

    companion object {
        fun newInstance(track: Track) = TrackVideosFragment().apply {
            arguments = Bundle().apply { putParcelable(MvRx.KEY_ARG, track) }
        }
    }
}
