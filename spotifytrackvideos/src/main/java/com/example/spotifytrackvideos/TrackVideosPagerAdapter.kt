package com.example.spotifytrackvideos

import com.example.coreandroid.view.viewpager.adapter.CurrentFragmentStatePagerAdapter

class TrackVideosPagerAdapter(
        manager: androidx.fragment.app.FragmentManager,
        val fragments: Array<androidx.fragment.app.Fragment>
) : CurrentFragmentStatePagerAdapter(manager) {

    override fun getItem(position: Int): androidx.fragment.app.Fragment = fragments[position]

    override fun getCount(): Int = fragments.size
}