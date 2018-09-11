package com.example.there.findclips.fragment.favourites

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.example.there.findclips.view.viewpager.adapter.CurrentFragmentStatePagerAdapter

class FavouritesFragmentPagerAdapter(
        fragmentManager: FragmentManager,
        private val fragments: Array<Fragment>
) : CurrentFragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size
}