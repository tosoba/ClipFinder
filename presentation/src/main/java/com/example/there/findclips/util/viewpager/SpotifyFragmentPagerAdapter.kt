package com.example.there.findclips.util.viewpager

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager


class SpotifyFragmentPagerAdapter(
        fragmentManager: FragmentManager,
        private val fragments: Array<Fragment>
) : CurrentFragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size
}