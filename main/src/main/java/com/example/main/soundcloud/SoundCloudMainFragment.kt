package com.example.main.soundcloud

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
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
import com.example.main.databinding.FragmentSoundcloudMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_soundcloud_main.*
import org.koin.android.ext.android.inject

class SoundCloudMainFragment : Fragment(), IMainContentFragment {

    private val fragmentFactory: IFragmentFactory by inject()

    private val itemIds: Array<Int> = arrayOf(R.id.sound_cloud_action_dashboard)

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        if (item.itemId == sound_cloud_bottom_navigation_view.selectedItemId) {
            currentNavHostFragment?.popAll()
            return@OnNavigationItemSelectedListener true
        }

        sound_cloud_view_pager.currentItem = itemIds.indexOf(item.itemId)
        activity?.castAs<ToolbarController>()?.toggleToolbar()

        return@OnNavigationItemSelectedListener true
    }

    private val pagerAdapter by lazy {
        CustomCurrentStatePagerAdapter(
            childFragmentManager,
            arrayOf(
                fragmentFactory.newSoundCloudDashboardNavHostFragment
            )
        )
    }

    private val onPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            sound_cloud_bottom_navigation_view?.checkItem(itemIds[position])
            activity?.castAs<ToolbarController>()?.toggleToolbar()
        }
    }

    override val currentFragment: Fragment?
        get() = pagerAdapter.currentFragment

    override val currentNavHostFragment: BaseNavHostFragment?
        get() = pagerAdapter.currentFragment?.castAs<BaseNavHostFragment>()

    override val playButton: FloatingActionButton get() = sound_cloud_play_fab

    private val view: SoundCloudMainView by lazy {
        SoundCloudMainView(
            onNavigationItemSelectedListener = onNavigationItemSelectedListener,
            pagerAdapter = pagerAdapter,
            onPageChangeListener = onPageChangeListener,
            offScreenPageLimit = 1
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = DataBindingUtil.inflate<FragmentSoundcloudMainBinding>(
        inflater, R.layout.fragment_soundcloud_main, container, false
    ).apply {
        fragmentView = view
    }.root
}
