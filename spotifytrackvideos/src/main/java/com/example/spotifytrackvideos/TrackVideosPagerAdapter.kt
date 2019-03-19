package com.example.spotifytrackvideos

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.example.coreandroid.view.viewpager.adapter.CurrentFragmentStatePagerAdapter

class TrackVideosPagerAdapter(
        manager: FragmentManager,
        val fragments: Array<Fragment>
) : CurrentFragmentStatePagerAdapter(manager) {

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size
}