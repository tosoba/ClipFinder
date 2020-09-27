package com.example.spotifyfavourites.spotify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.core.android.base.fragment.BaseListFragment
import com.example.core.android.base.fragment.BaseVMFragment
import com.example.core.android.model.spotify.Album
import com.example.core.android.model.spotify.Artist
import com.example.core.android.model.spotify.Playlist
import com.example.core.android.model.spotify.Track
import com.example.core.android.view.OnPageChangeListener
import com.example.core.android.view.OnTabSelectedListener
import com.example.core.android.view.viewpager.adapter.CustomCurrentStatePagerAdapter
import com.example.itemlist.spotify.SpotifyAlbumsFragment
import com.example.itemlist.spotify.SpotifyArtistsFragment
import com.example.itemlist.spotify.SpotifyPlaylistsFragment
import com.example.itemlist.spotify.SpotifyTracksFragment
import com.example.spotifyfavourites.R
import com.example.spotifyfavourites.databinding.FragmentSpotifyFavouritesBinding
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_spotify_favourites.*

class SpotifyFavouritesFragment : BaseVMFragment<SpotifyFavouritesViewModel>(SpotifyFavouritesViewModel::class) {

    private val onSpotifyTabSelectedListener = object : OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.let { spotify_favourites_tab_view_pager?.currentItem = it.position }
        }
    }

    private val onSpotifyPageChangedListener = object : OnPageChangeListener {
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

    private val view: SpotifyFavouritesFragmentView by lazy {
        SpotifyFavouritesFragmentView(
            pagerAdapter = pagerAdapter,
            onTabSelectedListener = onSpotifyTabSelectedListener,
            onPageChangeListener = onSpotifyPageChangedListener
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentSpotifyFavouritesBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_spotify_favourites,
            container,
            false
        )
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
                is SpotifyPlaylistsFragment -> fragment.updateItems(it.playlists)
                is SpotifyTracksFragment -> fragment.updateItems(it.tracks)
            }
        }
    }
}
