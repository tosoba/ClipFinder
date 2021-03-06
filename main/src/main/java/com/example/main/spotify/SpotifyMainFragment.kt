package com.example.main.spotify

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.coreandroid.base.IFragmentFactory
import com.example.coreandroid.base.fragment.BaseNavHostFragment
import com.example.coreandroid.base.fragment.IMainContentFragment
import com.example.coreandroid.di.Injectable
import com.example.coreandroid.util.ext.checkItem
import com.example.coreandroid.util.ext.toolbarController
import com.example.coreandroid.view.OnPageChangeListener
import com.example.coreandroid.view.viewpager.adapter.CustomCurrentStatePagerAdapter
import com.example.main.R
import com.example.main.databinding.FragmentSpotifyMainBinding
import kotlinx.android.synthetic.main.fragment_spotify_main.*
import javax.inject.Inject


class SpotifyMainFragment : Fragment(), IMainContentFragment, Injectable {

    @Inject
    lateinit var fragmentFactory: IFragmentFactory

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
                arrayOf(
                        fragmentFactory.newSpotifyDashboardNavHostFragment,
                        fragmentFactory.newSpotifyAccountNavHostFragment,
                        fragmentFactory.newSpotifyFavouritesMainNavHostFragment
                )
        )
    }

    private val onPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            main_bottom_navigation_view?.checkItem(itemIds[position])
            toolbarController?.toggleToolbar()
        }
    }

    override val currentFragment: Fragment?
        get() = pagerAdapter.currentFragment

    override val currentNavHostFragment: BaseNavHostFragment?
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
    ): View? = DataBindingUtil.inflate<FragmentSpotifyMainBinding>(
            inflater,
            R.layout.fragment_spotify_main,
            container,
            false
    ).apply {
        fragmentView = view
    }.root
}
