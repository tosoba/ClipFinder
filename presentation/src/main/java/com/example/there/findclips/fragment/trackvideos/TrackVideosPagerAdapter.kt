package com.example.there.findclips.fragment.trackvideos

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.example.there.findclips.view.viewpager.adapter.CurrentFragmentStatePagerAdapter

class TrackVideosPagerAdapter(manager: FragmentManager,
                              val fragments: Array<Fragment>) : CurrentFragmentStatePagerAdapter(manager) {
    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size
}