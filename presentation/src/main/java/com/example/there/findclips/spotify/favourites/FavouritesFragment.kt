package com.example.there.findclips.spotify.favourites

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.*
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.HasMainToolbar
import com.example.there.findclips.databinding.FragmentFavouritesBinding
import com.example.there.findclips.spotify.favourites.spotify.SpotifyFavouritesFragment
import com.example.there.findclips.util.ext.*
import com.example.there.findclips.videos.favourites.VideosFavouritesFragment
import kotlinx.android.synthetic.main.fragment_favourites.*


class FavouritesFragment : Fragment(), HasMainToolbar {

    override val toolbar: Toolbar
        get() = favourites_toolbar

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

    private val view: FavouritesView by lazy {
        FavouritesView(
                pagerAdapter,
                onNavigationItemSelectedListener,
                1,
                AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
                    favourites_bottom_navigation_view?.translationY = (-1 * verticalOffset).toFloat()
                }
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentFavouritesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourites, container, false)
        return binding.apply {
            favouritesFragmentView = view
            appCompatActivity?.setSupportActionBar(favouritesToolbar)
            appCompatActivity?.showDrawerHamburger()
            favouritesBottomNavigationView.setHeight(activity!!.dpToPx(40f).toInt())
        }.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        if (toolbar.menu?.size() == 0) appCompatActivity?.setSupportActionBar(toolbar)
    }

    override fun onOptionsItemSelected(
            item: MenuItem?
    ): Boolean = if (item?.itemId == android.R.id.home && parentFragment?.childFragmentManager?.backStackEntryCount == 0) {
        navigationDrawerController?.openDrawer()
        true
    } else false
}
