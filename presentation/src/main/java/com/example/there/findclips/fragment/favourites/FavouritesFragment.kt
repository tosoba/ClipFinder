package com.example.there.findclips.fragment.favourites

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.databinding.FragmentFavouritesBinding
import com.example.there.findclips.fragment.favourites.spotify.SpotifyFavouritesFragment
import com.example.there.findclips.fragment.favourites.videos.VideosFavouritesFragment
import com.example.there.findclips.util.ext.dpToPx
import com.example.there.findclips.util.ext.setHeight
import kotlinx.android.synthetic.main.fragment_favourites.*


class FavouritesFragment : Fragment() {

    private val pagerAdapter: FavouritesFragmentPagerAdapter by lazy {
        FavouritesFragmentPagerAdapter(childFragmentManager, arrayOf(
                SpotifyFavouritesFragment(),
                VideosFavouritesFragment()
        ))
    }

    private val itemIds: Array<Int> = arrayOf(R.id.action_spotify, R.id.action_videos)

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        if (item.itemId == favourites_bottom_navigation_view?.selectedItemId) {
            return@OnNavigationItemSelectedListener false
        }

        favourites_fragment_view_pager?.currentItem = itemIds.indexOf(item.itemId)

        return@OnNavigationItemSelectedListener true
    }

    private val view: FavouritesView by lazy { FavouritesView(pagerAdapter, onNavigationItemSelectedListener) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentFavouritesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourites, container, false)
        binding.favouritesFragmentView = view
        binding.favouritesFragmentViewPager.offscreenPageLimit = 1
        binding.favouritesBottomNavigationView.setHeight(activity!!.dpToPx(40f).toInt())
        return binding.root
    }
}
