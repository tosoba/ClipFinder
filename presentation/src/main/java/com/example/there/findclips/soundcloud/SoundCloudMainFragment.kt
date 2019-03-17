package com.example.there.findclips.soundcloud

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.soundcloud.dashboard.SoundCloudDashboardNavHostFragment
import com.example.there.findclips.soundcloud.favourites.SoundCloudFavouritesNavHostFragment
import com.example.there.findclips.util.ext.checkItem
import com.example.there.findclips.view.OnPageChangeListener
import com.example.there.findclips.view.viewpager.adapter.CustomCurrentStatePagerAdapter
import kotlinx.android.synthetic.main.fragment_soundcloud_main.*


class SoundCloudMainFragment : Fragment(), com.example.coreandroid.base.fragment.IMainContentFragment {

    private val itemIds: Array<Int> = arrayOf(R.id.sound_cloud_action_dashboard, R.id.sound_cloud_action_favorites)

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        if (item.itemId == sound_cloud_bottom_navigation_view.selectedItemId) {
            return@OnNavigationItemSelectedListener false
        }

        sound_cloud_view_pager.currentItem = itemIds.indexOf(item.itemId)
        toolbarController?.toggleToolbar()

        return@OnNavigationItemSelectedListener true
    }

    private val pagerAdapter by lazy {
        CustomCurrentStatePagerAdapter(
                childFragmentManager,
                arrayOf(SoundCloudDashboardNavHostFragment(), SoundCloudFavouritesNavHostFragment())
        )
    }

    private val onPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            sound_cloud_bottom_navigation_view?.checkItem(itemIds[position])
            toolbarController?.toggleToolbar()
        }
    }

    val currentFragment: Fragment?
        get() = pagerAdapter.currentFragment

    override val currentNavHostFragment: com.example.coreandroid.base.fragment.BaseNavHostFragment?
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
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = DataBindingUtil.inflate<com.example.there.findclips.databinding.FragmentSoundcloudMainBinding>(
            inflater,
            R.layout.fragment_soundcloud_main,
            container,
            false
    ).apply {
        fragmentView = view
    }.root
}
