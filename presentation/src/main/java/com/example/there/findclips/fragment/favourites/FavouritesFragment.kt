package com.example.there.findclips.fragment.favourites

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.example.there.findclips.R
import com.example.there.findclips.databinding.FragmentFavouritesBinding
import com.example.there.findclips.fragment.favourites.spotify.SpotifyFavouritesFragment
import com.example.there.findclips.fragment.favourites.videos.VideosFavouritesFragment
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
}
