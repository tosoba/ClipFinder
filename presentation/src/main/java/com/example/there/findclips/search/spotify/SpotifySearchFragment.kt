package com.example.there.findclips.search.spotify

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
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
import javax.inject.Inject


class SpotifySearchFragment : BaseSpotifyVMFragment<SpotifySearchViewModel>(), MainSearchFragment {

    override val title: String
        get() = "Search"

    override var query: String = ""
        set(value) {
            if (field == value) return
            field = value
            mainViewModel.searchAll(activity?.accessToken, query)
            mainViewModel.viewState.clearAll()
        }

    @Inject
    lateinit var spotifySearchVMFactory: SpotifySearchVMFactory

    private val onSpotifyTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) = Unit

        override fun onTabUnselected(tab: TabLayout.Tab?) = Unit

        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.let { spotify_tab_view_pager?.currentItem = it.position }
        }
    }

    private val onSpotifyPageChangedListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) = Unit

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

        override fun onPageSelected(position: Int) {
            spotify_tab_layout.getTabAt(position)?.select()
        }
    }

    private val view: SpotifySearchView by lazy {
        SpotifySearchView(
                state = mainViewModel.viewState,
                pagerAdapter = SpotifyFragmentPagerAdapter(childFragmentManager),
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
        mainViewModel = ViewModelProviders.of(this, spotifySearchVMFactory).get(SpotifySearchViewModel::class.java)
    }
}
