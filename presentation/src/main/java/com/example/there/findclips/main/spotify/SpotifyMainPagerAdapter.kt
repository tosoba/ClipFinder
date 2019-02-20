package com.example.there.findclips.main.spotify

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.example.there.findclips.base.fragment.BaseHostFragment
import com.example.there.findclips.fragment.account.AccountHostFragment
import com.example.there.findclips.fragment.dashboard.DashboardHostFragment
import com.example.there.findclips.fragment.favourites.FavouritesHostFragment
import com.example.there.findclips.view.viewpager.adapter.CurrentFragmentStatePagerAdapter

class SpotifyMainPagerAdapter(manager: FragmentManager) : CurrentFragmentStatePagerAdapter(manager) {

    private val fragments: Array<Fragment> = arrayOf(DashboardHostFragment(), AccountHostFragment(), FavouritesHostFragment())

    val currentHostFragment: BaseHostFragment?
        get() = currentFragment as? BaseHostFragment

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size
}