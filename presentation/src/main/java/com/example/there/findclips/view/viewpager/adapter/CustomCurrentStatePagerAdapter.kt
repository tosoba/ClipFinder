package com.example.there.findclips.view.viewpager.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager


open class CustomCurrentStatePagerAdapter(
        fragmentManager: FragmentManager,
        private val fragments: Array<Fragment>
) : CurrentFragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size
}