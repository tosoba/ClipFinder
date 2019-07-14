package com.example.main.soundcloud

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
import com.example.main.databinding.FragmentSoundcloudMainBinding
import kotlinx.android.synthetic.main.fragment_soundcloud_main.*
import javax.inject.Inject


class SoundCloudMainFragment : Fragment(), IMainContentFragment, Injectable {

    @Inject
    lateinit var fragmentFactory: IFragmentFactory

    private val itemIds: Array<Int> = arrayOf(R.id.sound_cloud_action_dashboard, R.id.sound_cloud_action_favorites)

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        if (item.itemId == sound_cloud_bottom_navigation_view.selectedItemId)
            return@OnNavigationItemSelectedListener false

        sound_cloud_view_pager.currentItem = itemIds.indexOf(item.itemId)
        toolbarController?.toggleToolbar()

        return@OnNavigationItemSelectedListener true
    }

    private val pagerAdapter by lazy {
        CustomCurrentStatePagerAdapter(
                childFragmentManager,
                arrayOf(fragmentFactory.newSoundCloudDashboardNavHostFragment, fragmentFactory.newSoundCloudFavouritesNavHostFragment)
        )
    }

    private val onPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            sound_cloud_bottom_navigation_view?.checkItem(itemIds[position])
            toolbarController?.toggleToolbar()
        }
    }

    override val currentFragment: Fragment?
        get() = pagerAdapter.currentFragment

    override val currentNavHostFragment: BaseNavHostFragment?
        get() = pagerAdapter.currentNavHostFragment

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
    ).apply { fragmentView = view }.root
}
