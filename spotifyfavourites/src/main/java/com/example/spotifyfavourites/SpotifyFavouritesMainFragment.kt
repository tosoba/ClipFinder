package com.example.spotifyfavourites

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.core.ext.castAs
import com.example.coreandroid.base.IFragmentFactory
import com.example.coreandroid.base.fragment.HasMainToolbar
import com.example.coreandroid.util.ext.*
import com.example.coreandroid.view.viewpager.adapter.CustomCurrentStatePagerAdapter
import com.example.spotifyfavourites.databinding.FragmentSpotifyFavouritesMainBinding
import com.example.spotifyfavourites.spotify.SpotifyFavouritesFragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_spotify_favourites_main.*
import org.koin.android.ext.android.inject

class SpotifyFavouritesMainFragment : Fragment(), HasMainToolbar {

    private val fragmentFactory: IFragmentFactory by inject()

    override val toolbar: Toolbar get() = favourites_toolbar

    private val pagerAdapter: CustomCurrentStatePagerAdapter by lazy {
        CustomCurrentStatePagerAdapter(childFragmentManager, arrayOf(
            SpotifyFavouritesFragment(),
            fragmentFactory.newVideosFavouritesFragment
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
            //TODO: fix this - it broke after change with hiding main bottom nav
            AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
                favourites_bottom_navigation_view?.translationY = (-1 * verticalOffset).toFloat()
            }
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentSpotifyFavouritesMainBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_spotify_favourites_main,
            container,
            false
        )
        mainContentFragment?.disablePlayButton()
        return binding.apply {
            favouritesFragmentView = view
            requireActivity().let {
                it.castAs<AppCompatActivity>()?.apply {
                    setSupportActionBar(favouritesToolbar)
                    showDrawerHamburger()
                }
                favouritesBottomNavigationView.setHeight(it.dpToPx(40f).toInt())
            }
        }.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        if (toolbar.menu?.size() == 0) {
            activity?.castAs<AppCompatActivity>()?.setSupportActionBar(toolbar)
        }
    }

    override fun onOptionsItemSelected(
        item: MenuItem?
    ): Boolean = if (item?.itemId == android.R.id.home && parentFragment?.childFragmentManager?.backStackEntryCount == 0) {
        navigationDrawerController?.openDrawer()
        true
    } else false
}
