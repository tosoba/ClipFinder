package com.example.spotifytrackvideos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.args
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.example.core.android.base.IFragmentFactory
import com.example.core.android.base.fragment.GoesToPreviousStateOnBackPressed
import com.example.core.android.base.fragment.ISearchFragment
import com.example.core.android.base.trackvideos.TrackVideosViewState
import com.example.core.android.spotify.controller.SpotifyTrackController
import com.example.core.android.spotify.ext.enableSpotifyPlayButton
import com.example.core.android.spotify.fragment.ISpotifyTrackFragment
import com.example.core.android.spotify.model.Track
import com.example.core.android.spotify.navigation.ISpotifyFragmentsFactory
import com.example.core.android.util.ext.backPressedWithNoPreviousStateController
import com.example.core.android.util.ext.loadBackgroundGradient
import com.example.core.android.util.ext.newFragmentWithMvRxArg
import com.example.core.android.util.ext.setupWithBackNavigation
import com.example.core.android.view.OnPageChangeListener
import com.example.core.android.view.viewpager.adapter.TitledCustomCurrentStatePagerAdapter
import com.example.spotifytrackvideos.databinding.FragmentTrackVideosBinding
import com.wada811.lifecycledispose.disposeOnDestroy
import org.koin.android.ext.android.inject

class TrackVideosFragment :
    BaseMvRxFragment(),
    GoesToPreviousStateOnBackPressed,
    SpotifyTrackController {

    override fun updateTrack(track: Track) {
        viewModel.updateTrack(track)
    }

    private val viewModel: TrackVideosViewModel by fragmentViewModel()

    private val fragmentFactory: IFragmentFactory by inject()
    private val spotifyFragmentFactory: ISpotifyFragmentsFactory by inject()

    private val argTrack: Track by args()

    private val pagerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        TitledCustomCurrentStatePagerAdapter(
            fragmentManager = childFragmentManager,
            titledFragments = arrayOf(
                getString(R.string.clips) to fragmentFactory.newVideosSearchFragment(argTrack.query),
                getString(R.string.info) to spotifyFragmentFactory.newSpotifyTrackFragment(argTrack)
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

        val currentTrack = MutableLiveData(argTrack)
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
                    withCurrentTrack(::updateCurrentFragment)
                }
            })
            trackVideosTabLayout.setupWithViewPager(trackVideosViewpager)
            trackVideosToolbar.setupWithBackNavigation(
                requireActivity() as? AppCompatActivity,
                ::onBackPressed
            )
            trackFavouriteFab.setOnClickListener { }
        }.root
    }

    override fun onBackPressed() = viewModel.onBackPressed()

    override fun onOptionsItemSelected(item: MenuItem): Boolean = false

    override fun invalidate() = Unit

    private fun updateCurrentFragment(newTrack: Track) {
        when (val currentFragment = pagerAdapter.currentFragment) {
            is ISearchFragment -> currentFragment.onNewQuery(newTrack.query)
            is ISpotifyTrackFragment -> currentFragment.onNewTrack(newTrack)
        }
    }

    companion object {
        fun new(track: Track): TrackVideosFragment = newFragmentWithMvRxArg(track)
    }
}
