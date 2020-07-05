package com.example.spotifytrackvideos

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.coreandroid.view.viewpager.adapter.CurrentFragmentStatePagerAdapter

class TrackVideosPagerAdapter(
    manager: FragmentManager,
    val fragments: Array<Fragment>
) : CurrentFragmentStatePagerAdapter(manager) {
    override fun getItem(position: Int): Fragment = fragments[position]
    override fun getCount(): Int = fragments.size
}
