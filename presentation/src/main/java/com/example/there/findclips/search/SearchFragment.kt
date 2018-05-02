package com.example.there.findclips.search

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.there.findclips.R
import com.example.there.findclips.base.BaseSpotifyVMFragment
import com.example.there.findclips.databinding.FragmentSearchBinding
import com.example.there.findclips.entities.Video
import com.example.there.findclips.lists.VideosList
import com.example.there.findclips.main.MainFragment
import com.example.there.findclips.search.spotify.SpotifyFragmentStatePagerAdapter
import com.example.there.findclips.search.spotify.SpotifySearchVMFactory
import com.example.there.findclips.search.spotify.SpotifySearchViewModel
import com.example.there.findclips.search.videos.VideosSearchVMFactory
import com.example.there.findclips.search.videos.VideosSearchViewModel
import com.example.there.findclips.util.*
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject


class SearchFragment : BaseSpotifyVMFragment<SpotifySearchViewModel>(), MainFragment {

    override val bottomNavigationItemId: Int
        get() = R.id.action_search

    @Inject
    lateinit var spotifySearchVMFactory: SpotifySearchVMFactory

    @Inject
    lateinit var videosSearchVMFactory: VideosSearchVMFactory

    private lateinit var videosSearchViewModel: VideosSearchViewModel

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

    private val videoItemClickListener = object : VideosList.OnItemClickListener {
        override fun onClick(item: Video) {

        }
    }

    private val view: SearchFragmentView
        get() = SearchFragmentView(
                state = searchViewState,
                videosSearchViewState = videosSearchViewModel.viewState,
                spotifySearchViewState = mainViewModel.viewState,
                videosAdapter = VideosList.Adapter(videosSearchViewModel.viewState.videos, R.layout.video_item, videoItemClickListener),
                videosLayoutManager = if (context?.screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                    GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                } else {
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                },
                videosItemDecoration = SeparatorDecoration(context!!, context!!.resources.getColor(R.color.colorAccent), 2f),
                pagerAdapter = SpotifyFragmentStatePagerAdapter(childFragmentManager),
                onTabSelectedListener = onSpotifyTabSelectedListener,
                onPageChangeListener = onSpotifyPageChangedListener
        )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentSearchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding.searchFragmentView = view
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if (savedInstanceState == null) initFromArguments()
        initFromSavedState(savedInstanceState)
    }

    private fun initFromArguments() {
        arguments?.let {
            if (it.containsKey(ARG_YOUTUBE_QUERY)) {
                searchViewState = SearchViewState(spinnerSelection = ObservableField(1),
                        queryHint = ObservableField(SearchViewState.SEARCH_YOUTUBE))
                videosSearchViewModel.getVideos(query = it.getString(ARG_YOUTUBE_QUERY))
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_SEARCH_VIEW_STATE, searchViewState)
    }

    private fun initFromSavedState(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            if (it.containsKey(KEY_SEARCH_VIEW_STATE)) {
                searchViewState = it.getParcelable(KEY_SEARCH_VIEW_STATE)
            }
        }
    }

    private var searchViewState: SearchViewState = SearchViewState()

    private val onMenuItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) = Unit

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            searchViewState.setSpinnerSelection(position)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        fun initSpinner(menu: Menu?) {
            val spinnerItem = menu?.findItem(R.id.spinner_menu_item)
            val menuSpinner = spinnerItem?.actionView as? Spinner

            menuSpinner?.let {
                it.adapter = ArrayAdapter<String>(context, R.layout.spinner_item, menuItems)
                it.setSelection(searchViewState.spinnerSelection.get() ?: 0)
                it.onItemSelectedListener = onMenuItemSelectedListener
            }
        }

        fun initSearchView(menu: Menu?) {
            val searchViewItem = menu?.findItem(R.id.search_view_menu_item)
            val searchView = searchViewItem?.actionView as? SearchView

            searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    if (!searchView.isIconified) {
                        searchView.isIconified = true
                    }
                    searchViewItem.collapseActionView()

                    processQuery(query)

                    return false
                }

                override fun onQueryTextChange(s: String): Boolean = true

                fun processQuery(query: String) {
                    fun shouldSearchSpotify(query: String): Boolean {
                        return searchViewState.spinnerSelection.get() == 0 && query != searchViewState.lastQuery
                    }

                    fun shouldSearchYoutube(query: String): Boolean {
                        return searchViewState.spinnerSelection.get() == 1 && query != searchViewState.lastQuery
                    }

                    if (shouldSearchYoutube(query)) {
                        videosSearchViewModel.getVideos(query)
                        videosSearchViewModel.viewState.videos.clear()
                    } else if (shouldSearchSpotify(query)) {
                        mainViewModel.searchAll(activity?.accessToken, query)
                        mainViewModel.viewState.clearAll()
                    }
                }
            })
        }

        inflater?.inflate(R.menu.search_fragment_menu, menu)
        initSpinner(menu)
        initSearchView(menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun initViewModel() {
        mainViewModel = ViewModelProviders.of(this, spotifySearchVMFactory).get(SpotifySearchViewModel::class.java)
        videosSearchViewModel = ViewModelProviders.of(this, videosSearchVMFactory).get(VideosSearchViewModel::class.java)
    }

    override fun setupObservers() {
        super.setupObservers()
        videosSearchViewModel.errorState.observe(this, Observer { error ->
            error?.let {
                Toast.makeText(this.activity, it.messageOrDefault(), Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun initComponent() {
        activity?.app?.createSearchComponent()?.inject(this)
    }

    override fun releaseComponent() {
        activity?.app?.releaseSearchComponent()
    }

    companion object {
        private val menuItems = arrayOf("Spotify", "Youtube")

        private const val KEY_SEARCH_VIEW_STATE = "KEY_SEARCH_VIEW_STATE"

        private const val ARG_YOUTUBE_QUERY = "ARG_YOUTUBE_QUERY"

        fun newInstanceVideosSearch(query: String): SearchFragment {
            val argBundle = Bundle().apply {
                putString(ARG_YOUTUBE_QUERY, query)
            }
            return SearchFragment().apply { arguments = argBundle }
        }
    }
}
