package com.example.there.findclips.search

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.example.there.findclips.search.spotify.SpotifySearchFragment
import com.example.there.findclips.search.videos.VideosSearchFragment
import com.example.there.findclips.util.CurrentFragmentStatePagerAdapter

class SearchFragmentPagerAdapter(fragmentManager: FragmentManager) : CurrentFragmentStatePagerAdapter(fragmentManager) {
    private val fragments: Array<Fragment> = arrayOf(SpotifySearchFragment(), VideosSearchFragment())

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size
}