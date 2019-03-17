package com.example.there.findclips.spotify.dashboard


import android.support.v4.app.Fragment
import com.example.there.findclips.R


class SpotifyDashboardNavHostFragment : com.example.coreandroid.base.fragment.BaseNavHostFragment() {

    override val layoutId: Int = R.layout.fragment_spotify_dashboard_host

    override val backStackLayoutId: Int = R.id.dashboard_back_stack_layout

    override val mainFragment: Fragment
        get() = SpotifyDashboardFragment()
}
