package com.example.there.findclips.search

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.ViewGroup
import com.example.there.findclips.search.spotify.SpotifySearchFragment
import com.example.there.findclips.search.videos.VideosSearchFragment

class SearchFragmentPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    var currentFragment: Fragment? = null
        private set

    private val fragments: Array<Fragment> = arrayOf(SpotifySearchFragment(), VideosSearchFragment())

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        if (currentFragment !== `object`) {
            currentFragment = `object` as Fragment
        }
        super.setPrimaryItem(container, position, `object`)
    }
}