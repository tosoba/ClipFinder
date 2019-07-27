package com.example.spotifysearch.spotify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.coreandroid.R
import com.example.coreandroid.base.fragment.BaseListFragment
import com.example.coreandroid.base.fragment.BaseVMFragment
import com.example.coreandroid.base.fragment.ISearchFragment
import com.example.coreandroid.lifecycle.ConnectivityComponent
import com.example.coreandroid.model.spotify.Album
import com.example.coreandroid.model.spotify.Artist
import com.example.coreandroid.model.spotify.Playlist
import com.example.coreandroid.model.spotify.Track
import com.example.coreandroid.util.ext.connectivitySnackbarHost
import com.example.coreandroid.view.OnPageChangeListener
import com.example.coreandroid.view.OnTabSelectedListener
import com.example.coreandroid.view.viewpager.adapter.CustomCurrentStatePagerAdapter
import com.example.itemlist.spotify.SpotifyAlbumsFragment
import com.example.itemlist.spotify.SpotifyArtistsFragment
import com.example.itemlist.spotify.SpotifyPlaylistsFragment
import com.example.itemlist.spotify.SpotifyTracksFragment
import com.example.spotifysearch.databinding.FragmentSpotifySearchBinding
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_spotify_search.*

class SpotifySearchFragment :
        BaseVMFragment<SpotifySearchViewModel>(SpotifySearchViewModel::class),
        ISearchFragment {

    override var query: String = ""
        set(value) {
            if (field == value) return
            field = value
            loadData()
            viewModel.viewState.clearAll()
        }

    private val onSpotifyTabSelectedListener = object : OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.let { spotify_tab_view_pager?.currentItem = it.position }
        }
    }

    private val onSpotifyPageChangedListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            spotify_tab_layout?.getTabAt(position)?.select()
        }
    }

    private fun updateCurrentFragment() = with(pagerAdapter.currentFragment) {
        when (this) {
            is SpotifyAlbumsFragment -> updateItems(viewModel.viewState.albums)
            is SpotifyArtistsFragment -> updateItems(viewModel.viewState.artists)
            is SpotifyPlaylistsFragment -> updateItems(viewModel.viewState.playlists)
            is SpotifyTracksFragment -> updateItems(viewModel.viewState.tracks)
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.loadedFlag.observe(this, Observer { updateCurrentFragment() })
    }

    private val loadMore: () -> Unit = {
        viewModel.searchAll(query)
    }

    private val pagerAdapter: CustomCurrentStatePagerAdapter by lazy {
        CustomCurrentStatePagerAdapter(childFragmentManager, arrayOf(
                BaseListFragment.newInstance<SpotifyAlbumsFragment, Album>(
                        getString(R.string.no_albums_loaded_yet),
                        getString(R.string.search_for_some),
                        viewModel.viewState.albums
                ).apply {
                    refreshData = { fragment ->
                        fragment.updateItems(viewModel.viewState.albums)
                    }
                    loadMore = this@SpotifySearchFragment.loadMore
                },
                BaseListFragment.newInstance<SpotifyArtistsFragment, Artist>(
                        getString(R.string.no_artists_loaded_yet),
                        getString(R.string.search_for_some),
                        viewModel.viewState.artists
                ).apply {
                    refreshData = { fragment ->
                        fragment.updateItems(viewModel.viewState.artists)
                    }
                    loadMore = this@SpotifySearchFragment.loadMore
                },
                BaseListFragment.newInstance<SpotifyPlaylistsFragment, Playlist>(
                        getString(R.string.no_playlists_loaded_yet),
                        getString(R.string.search_for_some),
                        viewModel.viewState.playlists
                ).apply {
                    refreshData = { fragment ->
                        fragment.updateItems(viewModel.viewState.playlists)
                    }
                    loadMore = this@SpotifySearchFragment.loadMore
                },
                BaseListFragment.newInstance<SpotifyTracksFragment, Track>(
                        getString(R.string.no_tracks_loaded_yet),
                        getString(R.string.search_for_some),
                        viewModel.viewState.tracks
                ).apply {
                    refreshData = { fragment ->
                        fragment.updateItems(viewModel.viewState.tracks)
                    }
                    loadMore = this@SpotifySearchFragment.loadMore
                }
        ))
    }

    private val view: SpotifySearchView by lazy {
        SpotifySearchView(
                state = viewModel.viewState,
                pagerAdapter = pagerAdapter,
                onTabSelectedListener = onSpotifyTabSelectedListener,
                onPageChangeListener = onSpotifyPageChangedListener
        )
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = DataBindingUtil.inflate<FragmentSpotifySearchBinding>(
            inflater,
            com.example.spotifysearch.R.layout.fragment_spotify_search,
            container,
            false
    ).apply {
        spotifySearchView = view
        spotifyTabViewPager.offscreenPageLimit = 3
    }.root

    private val connectivityComponent: ConnectivityComponent by lazy {
        ConnectivityComponent(
                activity!!,
                {
                    query == "" || (viewModel.viewState.albums.isNotEmpty() &&
                            viewModel.viewState.artists.isNotEmpty() &&
                            viewModel.viewState.playlists.isNotEmpty() &&
                            viewModel.viewState.tracks.isNotEmpty())
                },
                connectivitySnackbarHost!!.connectivitySnackbarParentView!!,
                ::loadData
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(connectivityComponent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFromArguments()
    }

    private fun initFromArguments() = arguments?.let {
        if (it.containsKey(ARG_QUERY)) query = it.getString(ARG_QUERY)!!
    }

    private fun loadData() = viewModel.searchAll(query)

    companion object {
        private const val ARG_QUERY = "ARG_QUERY"

        fun newInstanceWithQuery(query: String) = SpotifySearchFragment().apply {
            arguments = Bundle().apply { putString(ARG_QUERY, query) }
        }
    }
}
