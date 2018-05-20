package com.example.there.findclips.fragments.favourites

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.there.findclips.R
import com.example.there.findclips.base.BaseVMFragment
import com.example.there.findclips.fragments.favourites.spotify.SpotifyFavouritesFragment
import com.example.there.findclips.fragments.favourites.videos.VideosFavouritesFragment
import com.example.there.findclips.util.addAllIfNotContains
import com.example.there.findclips.util.app
import javax.inject.Inject


class FavouritesFragment : BaseVMFragment<FavouritesViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFromSavedState(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadFavourites()
    }

    private fun initFromSavedState(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            currentSpinnerSelection = it.getInt(KEY_SAVED_CURRENT_SPINNER_SELECTION)
        }
    }

    private var currentSpinnerSelection = 0

    private fun showChildFavouritesFragment(position: Int) {
        when (position) {
            0 -> showSpotifyFavouritesFragment()
            1 -> showVideosFavouritesFragment()
        }
    }

    private fun showSpotifyFavouritesFragment() = childFragmentManager.beginTransaction()
            .replace(R.id.favourites_main_fragment_container, SpotifyFavouritesFragment().apply {
                updateState(albums = viewModel.albums.value ?: emptyList(),
                        artists = viewModel.artists.value ?: emptyList(),
                        categories = viewModel.categories.value ?: emptyList(),
                        playlists = viewModel.spotifyPlaylists.value ?: emptyList(),
                        tracks = viewModel.tracks.value ?: emptyList())
            }, TAG_FAVOURITES_FRAGMENT)
            .commit()

    private fun showVideosFavouritesFragment() = childFragmentManager.beginTransaction()
            .replace(R.id.favourites_main_fragment_container, VideosFavouritesFragment(), TAG_FAVOURITES_FRAGMENT)
            .commit()

    private val onMenuItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) = Unit

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            currentSpinnerSelection = position
            showChildFavouritesFragment(position)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SAVED_CURRENT_SPINNER_SELECTION, currentSpinnerSelection)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.favourites_fragment_menu, menu)
        val spinnerItem = menu?.findItem(R.id.favourites_spinner_menu_item)
        val menuSpinner = spinnerItem?.actionView as? Spinner

        menuSpinner?.let {
            it.adapter = ArrayAdapter<String>(context, R.layout.spinner_item, menuItems)
            it.setSelection(currentSpinnerSelection)
            it.onItemSelectedListener = onMenuItemSelectedListener
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        showChildFavouritesFragment(currentSpinnerSelection)
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    private val spotifyFavouritesFragment: SpotifyFavouritesFragment?
        get() = childFragmentManager?.findFragmentByTag(TAG_FAVOURITES_FRAGMENT) as? SpotifyFavouritesFragment

    override fun setupObservers() {
        super.setupObservers()
        with(viewModel) {
            albums.observe(this@FavouritesFragment, Observer {
                it?.let { spotifyFavouritesFragment?.state?.albums?.addAllIfNotContains(it) }
            })

            artists.observe(this@FavouritesFragment, Observer {
                it?.let { spotifyFavouritesFragment?.state?.artists?.addAllIfNotContains(it) }
            })

            categories.observe(this@FavouritesFragment, Observer {
                it?.let { spotifyFavouritesFragment?.state?.categories?.addAllIfNotContains(it) }
            })

            spotifyPlaylists.observe(this@FavouritesFragment, Observer {
                it?.let { spotifyFavouritesFragment?.state?.playlists?.addAllIfNotContains(it) }
            })

            tracks.observe(this@FavouritesFragment, Observer {
                it?.let { spotifyFavouritesFragment?.state?.tracks?.addAllIfNotContains(it) }
            })

            videoPlaylists.observe(this@FavouritesFragment, Observer {

            })
        }
    }

    override fun initComponent() {
        activity?.app?.createFavouritesComponent()?.inject(this)
    }

    @Inject
    lateinit var vmFactory: FavouritesVMFactory

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, vmFactory).get(FavouritesViewModel::class.java)
    }

    override fun releaseComponent() {
        activity?.app?.releaseFavouritesComponent()
    }

    companion object {
        private const val KEY_SAVED_CURRENT_SPINNER_SELECTION = "KEY_SAVED_CURRENT_SPINNER_SELECTION"

        private const val TAG_FAVOURITES_FRAGMENT = "TAG_FAVOURITES_FRAGMENT"

        private val menuItems = arrayOf("Spotify", "Youtube")
    }
}
