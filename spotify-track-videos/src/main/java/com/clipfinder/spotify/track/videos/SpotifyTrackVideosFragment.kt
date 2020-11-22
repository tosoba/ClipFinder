package com.clipfinder.spotify.track.videos

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
import com.clipfinder.spotify.track.videos.databinding.FragmentSpotifyTrackVideosBinding
import com.example.core.android.base.IFragmentFactory
import com.example.core.android.base.fragment.BackPressedHandler
import com.example.core.android.base.fragment.ISearchFragment
import com.example.core.android.base.trackvideos.TrackVideosViewState
import com.example.core.android.spotify.controller.SpotifyTrackController
import com.example.core.android.spotify.ext.enableSpotifyPlayButton
import com.example.core.android.spotify.fragment.ISpotifyTrackFragment
import com.example.core.android.spotify.model.Track
import com.example.core.android.spotify.navigation.ISpotifyFragmentsFactory
import com.example.core.android.util.ext.backPressedController
import com.example.core.android.util.ext.loadBackgroundGradient
import com.example.core.android.util.ext.newMvRxFragmentWith
import com.example.core.android.util.ext.setupWithBackNavigation
import com.example.core.android.view.viewpager.adapter.TitledCustomCurrentStatePagerAdapter
import com.jakewharton.rxbinding2.support.v4.view.RxViewPager
import com.wada811.lifecycledispose.disposeOnDestroy
import org.koin.android.ext.android.inject

class SpotifyTrackVideosFragment : BaseMvRxFragment(), BackPressedHandler, SpotifyTrackController {
    private val argTrack: Track by args()
    private val viewModel: SpotifyTrackVideosViewModel by fragmentViewModel()

    private val fragmentFactory: IFragmentFactory by inject()
    private val spotifyFragmentFactory: ISpotifyFragmentsFactory by inject()

    private val pagerAdapter: TitledCustomCurrentStatePagerAdapter by lazy(LazyThreadSafetyMode.NONE) {
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
        val binding = FragmentSpotifyTrackVideosBinding.inflate(inflater, container, false)

        val currentTrack = MutableLiveData(argTrack)
        viewModel.selectSubscribe(this, TrackVideosViewState<Track>::tracks) { tracks ->
            tracks.lastOrNull()?.let {
                currentTrack.value = it
                binding.trackVideosToolbarGradientBackgroundView
                    .loadBackgroundGradient(it.iconUrl)
                    .disposeOnDestroy(this)
                binding.executePendingBindings()
                updateCurrentFragment(it)
            } ?: backPressedController?.onBackPressedWithNoPreviousState()
        }

        fun withCurrentTrack(block: (Track) -> Unit) = withState(viewModel) { (tracks) ->
            tracks.lastOrNull()?.let(block)
        }

        enableSpotifyPlayButton { withCurrentTrack(::loadTrack) }

        return binding.apply {
            lifecycleOwner = this@SpotifyTrackVideosFragment
            track = currentTrack

            trackVideosViewpager.adapter = pagerAdapter
            trackVideosViewpager.offscreenPageLimit = pagerAdapter.count - 1
            RxViewPager.pageSelections(trackVideosViewpager)
                .skipInitialValue()
                .subscribe { withCurrentTrack(::updateCurrentFragment) }
                .disposeOnDestroy(this@SpotifyTrackVideosFragment)
            trackVideosTabLayout.setupWithViewPager(trackVideosViewpager)

            trackVideosToolbar.setupWithBackNavigation(
                requireActivity() as? AppCompatActivity,
                ::onBackPressed
            )
            trackFavouriteFab.setOnClickListener { }
        }.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = false

    override fun onBackPressed() = viewModel.onBackPressed()

    override fun invalidate() = Unit

    override fun updateTrack(track: Track) = viewModel.updateTrack(track)

    private fun updateCurrentFragment(newTrack: Track) {
        when (val currentFragment = pagerAdapter.currentFragment) {
            is ISearchFragment -> currentFragment.onNewQuery(newTrack.query)
            is ISpotifyTrackFragment -> currentFragment.onNewTrack(newTrack)
        }
    }

    companion object {
        fun new(track: Track): SpotifyTrackVideosFragment = newMvRxFragmentWith(track)
    }
}
