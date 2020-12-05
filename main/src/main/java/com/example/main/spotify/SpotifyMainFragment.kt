package com.example.main.spotify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.clipfinder.core.ext.castAs
import com.example.core.android.base.IFragmentFactory
import com.example.core.android.base.fragment.BaseNavHostFragment
import com.example.core.android.base.fragment.IMainContentFragment
import com.example.core.android.base.handler.ToolbarController
import com.example.core.android.util.ext.checkItem
import com.example.core.android.view.OnPageChangeListener
import com.example.core.android.view.viewpager.adapter.CustomCurrentStatePagerAdapter
import com.example.main.R
import com.example.main.databinding.FragmentSpotifyMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_spotify_main.*
import org.koin.android.ext.android.inject

class SpotifyMainFragment : Fragment(), IMainContentFragment {
    private val fragmentFactory: IFragmentFactory by inject()

    private val onNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            if (item.itemId == main_bottom_navigation_view.selectedItemId) {
                currentNavHostFragment?.popAll()
                return@OnNavigationItemSelectedListener true
            }

            main_view_pager.currentItem = itemIds.indexOf(item.itemId)
            activity?.castAs<ToolbarController>()?.toggleToolbar()

            return@OnNavigationItemSelectedListener true
        }

    private val pagerAdapter: CustomCurrentStatePagerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        CustomCurrentStatePagerAdapter(
            childFragmentManager,
            arrayOf(
                fragmentFactory.newSpotifyDashboardNavHostFragment,
                fragmentFactory.newSpotifyAccountNavHostFragment
            )
        )
    }

    private val onPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            main_bottom_navigation_view?.checkItem(itemIds[position])
            activity?.castAs<ToolbarController>()?.toggleToolbar()
        }
    }

    override val currentFragment: Fragment?
        get() = pagerAdapter.currentFragment

    override val currentNavHostFragment: BaseNavHostFragment?
        get() = pagerAdapter.currentFragment?.castAs<BaseNavHostFragment>()

    override val playButton: FloatingActionButton
        get() = spotify_play_fab

    private val view: SpotifyMainView by lazy(LazyThreadSafetyMode.NONE) {
        SpotifyMainView(
            onNavigationItemSelectedListener = onNavigationItemSelectedListener,
            pagerAdapter = pagerAdapter,
            onPageChangeListener = onPageChangeListener,
            offScreenPageLimit = 2
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = FragmentSpotifyMainBinding.inflate(inflater, container, false)
        .apply { fragmentView = view }
        .root

    companion object {
        private val itemIds: Array<Int> = arrayOf(R.id.action_dashboard, R.id.action_user)
    }
}
