package com.example.there.findclips.fragments.favourites.spotify

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.base.BaseVMFragment
import com.example.there.findclips.databinding.FragmentSpotifyFavouritesBinding
import com.example.there.findclips.fragments.lists.*
import com.example.there.findclips.util.app
import com.example.there.findclips.view.OnPageChangeListener
import com.example.there.findclips.view.OnTabSelectedListener
import kotlinx.android.synthetic.main.fragment_spotify_favourites.*
import javax.inject.Inject


class SpotifyFavouritesFragment : BaseVMFragment<SpotifyFavouritesViewModel>() {

    private val onSpotifyTabSelectedListener = object : OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.let { spotify_favourites_tab_view_pager?.currentItem = it.position }
        }
    }

    private val onSpotifyPageChangedListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            spotify_favourites_tab_layout?.getTabAt(position)?.select()
            updateCurrentFragment()
        }
    }

    private val pagerAdapter: SpotifyFragmentPagerAdapter by lazy {
        SpotifyFragmentPagerAdapter(childFragmentManager, arrayOf(
                SpotifyAlbumsFragment.newInstance(getString(R.string.no_favourite_albums_addded_yet), getString(R.string.browse_for_albums)),
                SpotifyArtistsFragment.newInstance(getString(R.string.no_favourite_artists_added_yet), getString(R.string.browse_for_artists)),
                SpotifyCategoriesFragment.newInstance(getString(R.string.no_favourite_categories_added_yet), getString(R.string.browse_for_categories)),
                SpotifyPlaylistsFragment.newInstance(getString(R.string.no_favourite_playlists_added_yet), getString(R.string.browse_for_playlists)),
                SpotifyTracksFragment.newInstance(getString(R.string.no_favourite_tracks_added_yet), getString(R.string.browse_for_tracks))
        ))
    }

    private val view: SpotifyFavouritesFragmentView by lazy {
        SpotifyFavouritesFragmentView(
                pagerAdapter = pagerAdapter,
                onTabSelectedListener = onSpotifyTabSelectedListener,
                onPageChangeListener = onSpotifyPageChangedListener
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentSpotifyFavouritesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_spotify_favourites, container, false)
        return binding.apply {
            this.view = this@SpotifyFavouritesFragment.view
        }.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadFavourites()
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.loadedFlag.observe(this, Observer { updateCurrentFragment() })
    }

    private fun updateCurrentFragment() {
        val fragment = pagerAdapter.currentFragment

        when (fragment) {
            is SpotifyAlbumsFragment -> fragment.addItems(viewModel.viewState.albums)
            is SpotifyArtistsFragment -> fragment.addItems(viewModel.viewState.artists)
            is SpotifyCategoriesFragment -> fragment.addItems(viewModel.viewState.categories)
            is SpotifyPlaylistsFragment -> fragment.addItems(viewModel.viewState.playlists)
            is SpotifyTracksFragment -> fragment.addItems(viewModel.viewState.tracks)
        }
    }

    override fun initComponent() {
        activity?.app?.createFavouritesSpotifyComponent()?.inject(this)
    }

    @Inject
    lateinit var vmFactory: SpotifyFavouritesVMFactory

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, vmFactory).get(SpotifyFavouritesViewModel::class.java)
    }

    override fun releaseComponent() {
        activity?.app?.releaseFavouritesSpotifyComponent()
    }
}
