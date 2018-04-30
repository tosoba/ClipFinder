package com.example.there.findclips.search.spotify

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.example.there.findclips.search.spotify.fragments.SpotifyAlbumsFragment
import com.example.there.findclips.search.spotify.fragments.SpotifyArtistsFragment
import com.example.there.findclips.search.spotify.fragments.SpotifyPlaylistsFragment
import com.example.there.findclips.search.spotify.fragments.SpotifyTracksFragment
import java.lang.IllegalArgumentException


class SpotifyFragmentStatePagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> SpotifyAlbumsFragment()
        1 -> SpotifyArtistsFragment()
        2 -> SpotifyPlaylistsFragment()
        3 -> SpotifyTracksFragment()
        else -> throw IllegalArgumentException("${javaClass.name}: Fragment at position $position not found.")
    }

    override fun getCount(): Int = FRAGMENTS_COUNT

    companion object {
        private const val FRAGMENTS_COUNT = 4
    }
}