package com.clipfinder.main.soundcloud

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.clipfinder.core.android.base.IFragmentFactory
import com.clipfinder.core.android.base.fragment.BaseNavHostFragment
import com.clipfinder.core.android.base.fragment.IMainContentFragment
import com.clipfinder.core.android.base.handler.ToolbarController
import com.clipfinder.core.android.util.ext.checkItem
import com.clipfinder.core.android.view.OnPageChangeListener
import com.clipfinder.core.android.view.viewpager.adapter.CustomCurrentStatePagerAdapter
import com.clipfinder.core.ext.castAs
import com.clipfinder.main.R
import com.clipfinder.main.databinding.FragmentSoundcloudMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_soundcloud_main.*
import org.koin.android.ext.android.inject

class SoundCloudMainFragment : Fragment(), IMainContentFragment {
    private val fragmentFactory: IFragmentFactory by inject()

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            if (item.itemId == sound_cloud_bottom_navigation_view.selectedItemId) {
                currentNavHostFragment?.popAll()
                return@OnNavigationItemSelectedListener true
            }

            sound_cloud_view_pager.currentItem = itemIds.indexOf(item.itemId)
            activity?.castAs<ToolbarController>()?.toggleToolbar()

            return@OnNavigationItemSelectedListener true
        }

    private val pagerAdapter by
        lazy(LazyThreadSafetyMode.NONE) {
            CustomCurrentStatePagerAdapter(
                childFragmentManager,
                arrayOf(fragmentFactory.newSoundCloudDashboardNavHostFragment)
            )
        }

    private val onPageChangeListener =
        object : OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                sound_cloud_bottom_navigation_view?.checkItem(itemIds[position])
                activity?.castAs<ToolbarController>()?.toggleToolbar()
            }
        }

    override val currentFragment: Fragment?
        get() = pagerAdapter.currentFragment

    override val currentNavHostFragment: BaseNavHostFragment?
        get() = pagerAdapter.currentFragment?.castAs<BaseNavHostFragment>()

    override val playButton: FloatingActionButton
        get() = sound_cloud_play_fab

    private val view: SoundCloudMainView by
        lazy(LazyThreadSafetyMode.NONE) {
            SoundCloudMainView(
                onNavigationItemSelectedListener = onNavigationItemSelectedListener,
                pagerAdapter = pagerAdapter,
                onPageChangeListener = onPageChangeListener,
                offScreenPageLimit = 1
            )
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentSoundcloudMainBinding.inflate(inflater, container, false)
            .apply { fragmentView = view }
            .root

    companion object {
        private val itemIds: Array<Int> = arrayOf(R.id.sound_cloud_action_dashboard)
    }
}
