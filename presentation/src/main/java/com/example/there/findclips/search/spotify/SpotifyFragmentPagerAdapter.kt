package com.example.there.findclips.search.spotify

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.example.there.findclips.search.spotify.fragments.SpotifyAlbumsFragment
import com.example.there.findclips.search.spotify.fragments.SpotifyArtistsFragment
import com.example.there.findclips.search.spotify.fragments.SpotifyPlaylistsFragment
import com.example.there.findclips.search.spotify.fragments.SpotifyTracksFragment


class SpotifyFragmentPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    private val fragments = arrayOf(
            SpotifyAlbumsFragment(),
            SpotifyArtistsFragment(),
            SpotifyPlaylistsFragment(),
            SpotifyTracksFragment()
    )

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size
}