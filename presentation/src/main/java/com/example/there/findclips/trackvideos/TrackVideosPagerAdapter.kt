package com.example.there.findclips.trackvideos

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.example.there.findclips.entities.Track
import com.example.there.findclips.search.videos.VideosSearchFragment
import com.example.there.findclips.track.TrackFragment
import com.example.there.findclips.util.CurrentFragmentStatePagerAdapter

class TrackVideosPagerAdapter(manager: FragmentManager,
                              val fragments: Array<Fragment>) : CurrentFragmentStatePagerAdapter(manager) {
    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size
}