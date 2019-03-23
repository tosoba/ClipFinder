package com.example.spotifyfavourites.spotify

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.coreandroid.base.fragment.BaseListFragment
import com.example.coreandroid.base.fragment.BaseVMFragment
import com.example.coreandroid.di.Injectable
import com.example.coreandroid.model.spotify.*
import com.example.coreandroid.view.viewpager.adapter.CustomCurrentStatePagerAdapter
import com.example.itemlist.spotify.*
import com.example.spotifyfavourites.R
import kotlinx.android.synthetic.main.fragment_spotify_favourites.*

class SpotifyFavouritesFragment : BaseVMFragment<SpotifyFavouritesViewModel>(SpotifyFavouritesViewModel::class.java), Injectable {

    private val onSpotifyTabSelectedListener = object : com.example.coreandroid.view.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.let { spotify_favourites_tab_view_pager?.currentItem = it.position }
        }
    }

    private val onSpotifyPageChangedListener = object : com.example.coreandroid.view.OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            spotify_favourites_tab_layout?.getTabAt(position)?.select()
        }
    }

    private val pagerAdapter: CustomCurrentStatePagerAdapter by lazy {
        CustomCurrentStatePagerAdapter(childFragmentManager, arrayOf(
                BaseListFragment.newInstance<SpotifyAlbumsFragment, Album>(
                        getString(R.string.no_favourite_albums_addded_yet),
                        getString(R.string.browse_for_albums),
                        viewModel.viewState.value?.albums
                ).apply {
                    refreshData = { fragment ->
                        viewModel.viewState.value?.let {
                            fragment.updateItems(it.albums)
                        }
                    }
                },
                BaseListFragment.newInstance<SpotifyArtistsFragment, Artist>(
                        getString(R.string.no_favourite_artists_added_yet),
                        getString(R.string.browse_for_artists),
                        viewModel.viewState.value?.artists
                ).apply {
                    refreshData = { fragment ->
                        viewModel.viewState.value?.let {
                            fragment.updateItems(it.artists)
                        }
                    }
                },
                BaseListFragment.newInstance<SpotifyCategoriesFragment, Category>(
                        getString(R.string.no_favourite_categories_added_yet),
                        getString(R.string.browse_for_categories),
                        viewModel.viewState.value?.categories
                ).apply {
                    refreshData = { fragment ->
                        viewModel.viewState.value?.let {
                            fragment.updateItems(it.categories)
                        }
                    }
                },
                BaseListFragment.newInstance<SpotifyPlaylistsFragment, Playlist>(
                        getString(R.string.no_favourite_playlists_added_yet),
                        getString(R.string.browse_for_playlists),
                        viewModel.viewState.value?.playlists
                ).apply {
                    refreshData = { fragment ->
                        viewModel.viewState.value?.let {
                            fragment.updateItems(it.playlists)
                        }
                    }
                },
                BaseListFragment.newInstance<SpotifyTracksFragment, Track>(
                        getString(R.string.no_favourite_tracks_added_yet),
                        getString(R.string.browse_for_tracks),
                        viewModel.viewState.value?.tracks
                ).apply {
                    refreshData = { fragment ->
                        viewModel.viewState.value?.let {
                            fragment.updateItems(it.tracks)
                        }
                    }
                }
        ))
    }

    private val view: com.example.spotifyfavourites.spotify.SpotifyFavouritesFragmentView by lazy {
        com.example.spotifyfavourites.spotify.SpotifyFavouritesFragmentView(
                pagerAdapter = pagerAdapter,
                onTabSelectedListener = onSpotifyTabSelectedListener,
                onPageChangeListener = onSpotifyPageChangedListener
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: com.example.spotifyfavourites.databinding.FragmentSpotifyFavouritesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_spotify_favourites, container, false)
        return binding.apply {
            this.view = this@SpotifyFavouritesFragment.view
        }.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadFavourites()
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.viewState.observe(this, Observer { updateCurrentFragment() })
    }

    private fun updateCurrentFragment() {
        val fragment = pagerAdapter.currentFragment

        viewModel.viewState.value?.let {
            when (fragment) {
                is SpotifyAlbumsFragment -> fragment.updateItems(it.albums)
                is SpotifyArtistsFragment -> fragment.updateItems(it.artists)
                is SpotifyCategoriesFragment -> fragment.updateItems(it.categories)
                is SpotifyPlaylistsFragment -> fragment.updateItems(it.playlists)
                is SpotifyTracksFragment -> fragment.updateItems(it.tracks)
            }
        }
    }
}
