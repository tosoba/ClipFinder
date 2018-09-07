package com.example.there.findclips.fragments.favourites

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.there.findclips.R
import com.example.there.findclips.databinding.FragmentFavouritesBinding
import com.example.there.findclips.fragments.favourites.spotify.SpotifyFavouritesFragment
import com.example.there.findclips.fragments.favourites.videos.VideosFavouritesFragment
import kotlinx.android.synthetic.main.fragment_favourites.*


class FavouritesFragment : Fragment() {

    private val onMenuItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) = Unit

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            favourites_fragment_view_pager?.currentItem = position
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)

        menu?.findItem(R.id.favourites_spinner_menu_item)?.isVisible = true
        menu?.findItem(R.id.spinner_menu_item)?.isVisible = false
        menu?.findItem(R.id.search_view_menu_item)?.isVisible = false
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.favourites_fragment_menu, menu)

        val spinnerItem = menu?.findItem(R.id.favourites_spinner_menu_item)
        val menuSpinner = spinnerItem?.actionView as? Spinner

        menuSpinner?.let {
            it.adapter = ArrayAdapter<String>(context, R.layout.spinner_item, menuItems)
            it.setSelection(favourites_fragment_view_pager.currentItem)
            it.onItemSelectedListener = onMenuItemSelectedListener
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    private val pagerAdapter: FavouritesFragmentPagerAdapter by lazy {
        FavouritesFragmentPagerAdapter(childFragmentManager, arrayOf(
                SpotifyFavouritesFragment(),
                VideosFavouritesFragment()
        ))
    }

    private val view: FavouritesView by lazy { FavouritesView(pagerAdapter) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentFavouritesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourites, container, false)
        binding.favouritesFragmentView = view
        binding.favouritesFragmentViewPager.offscreenPageLimit = 1
        return binding.root
    }

    companion object {
        private val menuItems = arrayOf("Spotify", "Youtube")
    }
}
