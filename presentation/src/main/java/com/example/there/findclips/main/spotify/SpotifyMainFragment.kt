package com.example.there.findclips.main.spotify

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseNavHostFragment
import com.example.there.findclips.fragment.account.AccountNavHostFragment
import com.example.there.findclips.fragment.dashboard.DashboardNavHostFragment
import com.example.there.findclips.fragment.favourites.FavouritesNavHostFragment
import com.example.there.findclips.util.ext.checkItem
import com.example.there.findclips.util.ext.toolbarController
import com.example.there.findclips.view.OnPageChangeListener
import com.example.there.findclips.view.viewpager.adapter.CustomCurrentStatePagerAdapter
import kotlinx.android.synthetic.main.fragment_spotify_main.*


class SpotifyMainFragment : Fragment() {

    private val itemIds: Array<Int> = arrayOf(R.id.action_dashboard, R.id.action_user, R.id.action_favorites)

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        if (item.itemId == main_bottom_navigation_view.selectedItemId) {
            return@OnNavigationItemSelectedListener false
        }

        main_view_pager.currentItem = itemIds.indexOf(item.itemId)
        toolbarController?.toggleToolbar()

        return@OnNavigationItemSelectedListener true
    }

    private val pagerAdapter by lazy {
        CustomCurrentStatePagerAdapter(
                childFragmentManager,
                arrayOf(DashboardNavHostFragment(), AccountNavHostFragment(), FavouritesNavHostFragment())
        )
    }

    private val onPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            main_bottom_navigation_view?.checkItem(itemIds[position])
            toolbarController?.toggleToolbar()
        }
    }

    val currentFragment: Fragment?
        get() = pagerAdapter.currentFragment

    val currentNavHostFragment: BaseNavHostFragment?
        get() = pagerAdapter.currentNavHostFragment

    private val view: SpotifyMainView by lazy {
        SpotifyMainView(
                onNavigationItemSelectedListener = onNavigationItemSelectedListener,
                pagerAdapter = pagerAdapter,
                onPageChangeListener = onPageChangeListener,
                offScreenPageLimit = 2
        )
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = DataBindingUtil.inflate<com.example.there.findclips.databinding.FragmentSpotifyMainBinding>(
            inflater,
            R.layout.fragment_spotify_main,
            container,
            false
    ).apply {
        fragmentView = view
    }.root
}
