package com.example.there.findclips.search.spotify

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.base.BaseSpotifyVMFragment
import com.example.there.findclips.search.MainSearchFragment
import com.example.there.findclips.util.accessToken
import com.example.there.findclips.util.app
import kotlinx.android.synthetic.main.fragment_spotify_search.*
import com.example.there.findclips.databinding.FragmentSpotifySearchBinding
import com.example.there.findclips.listfragments.SpotifyAlbumsFragment
import com.example.there.findclips.listfragments.SpotifyArtistsFragment
import com.example.there.findclips.listfragments.SpotifyPlaylistsFragment
import com.example.there.findclips.listfragments.SpotifyTracksFragment
import com.example.there.findclips.util.OnPageChangeListener
import com.example.there.findclips.util.OnTabSelectedListener
import javax.inject.Inject


class SpotifySearchFragment : BaseSpotifyVMFragment<SpotifySearchViewModel>(), MainSearchFragment {

    override var query: String = ""
        set(value) {
            if (field == value) return
            field = value
            viewModel.searchAll(activity?.accessToken, query)
            viewModel.viewState.clearAll()
        }

    @Inject
    lateinit var spotifySearchVMFactory: SpotifySearchVMFactory

    private val onSpotifyTabSelectedListener = object : OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.let { spotify_tab_view_pager?.currentItem = it.position }
        }
    }

    private val onSpotifyPageChangedListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            spotify_tab_layout?.getTabAt(position)?.select()
            updateCurrentFragment()
        }
    }

    private fun updateCurrentFragment() {
        val fragment = pagerAdapter.currentFragment

        when (fragment) {
            is SpotifyAlbumsFragment -> fragment.addItems(viewModel.viewState.albums)
            is SpotifyArtistsFragment -> fragment.addItems(viewModel.viewState.artists)
            is SpotifyPlaylistsFragment -> fragment.addItems(viewModel.viewState.playlists)
            is SpotifyTracksFragment -> fragment.addItems(viewModel.viewState.tracks)
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.loadedFlag.observe(this, Observer { updateCurrentFragment() })
    }

    private val pagerAdapter: SpotifyFragmentPagerAdapter by lazy { SpotifyFragmentPagerAdapter(childFragmentManager) }

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
        return binding.root
    }

    override fun initComponent() {
        activity?.app?.createSpotifySearchComponent()?.inject(this)
    }

    override fun releaseComponent() {
        activity?.app?.releaseSpotifySearchComponent()
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, spotifySearchVMFactory).get(SpotifySearchViewModel::class.java)
    }
}
