package com.example.there.findclips.spotify.favourites

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.*
import com.example.there.findclips.R
import com.example.there.findclips.spotify.favourites.spotify.SpotifyFavouritesFragment
import com.example.there.findclips.videos.favourites.VideosFavouritesFragment
import com.example.there.findclips.view.viewpager.adapter.CustomCurrentStatePagerAdapter
import kotlinx.android.synthetic.main.fragment_spotify_favourites_main.*


class SpotifyFavouritesMainFragment : Fragment(), com.example.coreandroid.base.fragment.HasMainToolbar {

    override val toolbar: Toolbar
        get() = favourites_toolbar

    private val pagerAdapter: CustomCurrentStatePagerAdapter by lazy {
        CustomCurrentStatePagerAdapter(childFragmentManager, arrayOf(
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

    private val view: SpotifyFavouritesMainView by lazy {
        SpotifyFavouritesMainView(
                pagerAdapter,
                onNavigationItemSelectedListener,
                1,
                AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
                    favourites_bottom_navigation_view?.translationY = (-1 * verticalOffset).toFloat()
                }
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentSpotifyFavouritesMainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_spotify_favourites_main, container, false)
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
