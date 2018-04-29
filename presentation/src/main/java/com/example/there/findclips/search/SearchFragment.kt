package com.example.there.findclips.search

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.*
import android.widget.*
import com.example.there.findclips.R
import com.example.there.findclips.base.BaseSpotifyVMFragment
import com.example.there.findclips.databinding.FragmentSearchBinding
import com.example.there.findclips.spotifysearch.SpotifySearchVMFactory
import com.example.there.findclips.spotifysearch.SpotifySearchViewModel
import com.example.there.findclips.util.app
import com.example.there.findclips.util.messageOrDefault
import com.example.there.findclips.videossearch.VideosSearchVMFactory
import com.example.there.findclips.videossearch.VideosSearchViewModel
import javax.inject.Inject


class SearchFragment : BaseSpotifyVMFragment<SpotifySearchViewModel>() {

    @Inject
    lateinit var spotifySearchVMFactory: SpotifySearchVMFactory

    @Inject
    lateinit var videosSearchVMFactory: VideosSearchVMFactory

    private lateinit var videosSearchViewModel: VideosSearchViewModel

    private val onQuerySearchListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            query?.let {
                if (shouldSearchYoutube(query)) {
                    videosSearchViewModel.getVideos(query)
                    videosSearchViewModel.viewState.videos.clear()
                } else if (shouldSearchSpotify(query)) {

                }
            }
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean = true

        fun shouldSearchYoutube(query: String): Boolean {
            return searchViewState.spinnerSelection.get() == 1 && query != searchViewState.lastQuery && query.isNotBlank()
        }

        fun shouldSearchSpotify(query: String): Boolean {
            return searchViewState.spinnerSelection.get() == 0 && query != searchViewState.lastQuery && query.isNotBlank()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentSearchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding.videosViewState = videosSearchViewModel.viewState
        binding.spotifyViewState = mainViewModel.viewState
        binding.searchViewState = searchViewState
        binding.onQueryTextListener = onQuerySearchListener
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        initFromSavedState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_SEARCH_VIEW_STATE, searchViewState)
    }

    private fun initFromSavedState(savedInstanceState: Bundle?) {
        searchViewState = savedInstanceState?.getParcelable(KEY_SEARCH_VIEW_STATE) ?: SearchViewState()
    }

    private var searchViewState: SearchViewState = SearchViewState()

    private lateinit var menuSpinner: Spinner

    private val onMenuItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) = Unit

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            searchViewState.spinnerSelection.set(position)
            searchViewState.updateQueryHint(position)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.search_spinner_menu, menu)

        val item = menu?.findItem(R.id.spinner)
        menuSpinner = item?.actionView as Spinner

        with(menuSpinner) {
            adapter = ArrayAdapter<String>(context, R.layout.spinner_item, menuItems)
            setSelection(searchViewState.spinnerSelection.get() ?: 0)
            onItemSelectedListener = onMenuItemSelectedListener
        }

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
    }
}
