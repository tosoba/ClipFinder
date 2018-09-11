package com.example.there.findclips.fragment.search.spotify

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseSpotifyVMFragment
import com.example.there.findclips.databinding.FragmentSpotifySearchBinding
import com.example.there.findclips.di.Injectable
import com.example.there.findclips.fragment.list.SpotifyAlbumsFragment
import com.example.there.findclips.fragment.list.SpotifyArtistsFragment
import com.example.there.findclips.fragment.list.SpotifyPlaylistsFragment
import com.example.there.findclips.fragment.list.SpotifyTracksFragment
import com.example.there.findclips.fragment.search.MainSearchFragment
import com.example.there.findclips.lifecycle.ConnectivityComponent
import com.example.there.findclips.util.ext.accessToken
import com.example.there.findclips.util.ext.mainActivity
import com.example.there.findclips.view.OnPageChangeListener
import com.example.there.findclips.view.OnTabSelectedListener
import com.example.there.findclips.view.viewpager.adapter.CustomCurrentStatePagerAdapter
import kotlinx.android.synthetic.main.fragment_spotify_search.*


class SpotifySearchFragment : BaseSpotifyVMFragment<SpotifySearchViewModel>(), MainSearchFragment, Injectable {

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

    private fun updateCurrentFragment() {
        val fragment = pagerAdapter.currentFragment

        when (fragment) {
            is SpotifyAlbumsFragment -> fragment.updateItems(viewModel.viewState.albums)
            is SpotifyArtistsFragment -> fragment.updateItems(viewModel.viewState.artists)
            is SpotifyPlaylistsFragment -> fragment.updateItems(viewModel.viewState.playlists)
            is SpotifyTracksFragment -> fragment.updateItems(viewModel.viewState.tracks)
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.loadedFlag.observe(this, Observer { updateCurrentFragment() })
    }

    private val pagerAdapter: CustomCurrentStatePagerAdapter by lazy {
        CustomCurrentStatePagerAdapter(childFragmentManager, arrayOf(
                SpotifyAlbumsFragment.newInstance(
                        getString(R.string.no_albums_loaded_yet),
                        getString(R.string.search_for_some),
                        viewModel.viewState.albums
                ).apply {
                    refreshData = { fragment ->
                        fragment.updateItems(viewModel.viewState.albums)
                    }
                },
                SpotifyArtistsFragment.newInstance(
                        getString(R.string.no_artists_loaded_yet),
                        getString(R.string.search_for_some),
                        viewModel.viewState.artists
                ).apply {
                    refreshData = { fragment ->
                        fragment.updateItems(viewModel.viewState.artists)
                    }
                },
                SpotifyPlaylistsFragment.newInstance(
                        getString(R.string.no_playlists_loaded_yet),
                        getString(R.string.search_for_some),
                        viewModel.viewState.playlists
                ).apply {
                    refreshData = { fragment ->
                        fragment.updateItems(viewModel.viewState.playlists)
                    }
                },
                SpotifyTracksFragment.newInstance(
                        getString(R.string.no_tracks_loaded_yet),
                        getString(R.string.search_for_some),
                        viewModel.viewState.tracks
                ).apply {
                    refreshData = { fragment ->
                        fragment.updateItems(viewModel.viewState.tracks)
                    }
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentSpotifySearchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_spotify_search, container, false)
        binding.spotifySearchView = view
        binding.spotifyTabViewPager.offscreenPageLimit = 3
        return binding.root
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(SpotifySearchViewModel::class.java)
    }

    private val connectivityComponent: ConnectivityComponent by lazy {
        ConnectivityComponent(
                activity!!,
                {
                    query == "" ||
                            (viewModel.viewState.albums.isNotEmpty() &&
                                    viewModel.viewState.artists.isNotEmpty() &&
                                    viewModel.viewState.playlists.isNotEmpty() &&
                                    viewModel.viewState.tracks.isNotEmpty())
                },
                mainActivity!!.connectivitySnackbarParentView!!,
                ::loadData,
                true
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

    private fun initFromArguments() {
        arguments?.let {
            if (it.containsKey(ARG_QUERY)) query = it.getString(ARG_QUERY)!!
        }
    }

    private fun loadData() = viewModel.searchAll(activity?.accessToken, query)

    companion object {
        private const val ARG_QUERY = "ARG_QUERY"

        fun newInstanceWithQuery(query: String) = SpotifySearchFragment().apply {
            arguments = Bundle().apply { putString(ARG_QUERY, query) }
        }
    }
}