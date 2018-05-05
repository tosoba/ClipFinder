package com.example.there.findclips.search.spotify

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.example.there.findclips.listfragments.SpotifyAlbumsFragment
import com.example.there.findclips.listfragments.SpotifyArtistsFragment
import com.example.there.findclips.listfragments.SpotifyPlaylistsFragment
import com.example.there.findclips.listfragments.SpotifyTracksFragment
import com.example.there.findclips.util.CurrentFragmentStatePagerAdapter


class SpotifyFragmentPagerAdapter(fragmentManager: FragmentManager) : CurrentFragmentStatePagerAdapter(fragmentManager) {

    val fragments: Array<Fragment> = arrayOf(
            SpotifyAlbumsFragment(),
            SpotifyArtistsFragment(),
            SpotifyPlaylistsFragment(),
            SpotifyTracksFragment()
    )

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size
}