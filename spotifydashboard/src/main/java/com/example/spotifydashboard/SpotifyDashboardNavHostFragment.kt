package com.example.spotifydashboard

import androidx.fragment.app.Fragment
import com.example.coreandroid.base.fragment.BaseNavHostFragment

class SpotifyDashboardNavHostFragment : BaseNavHostFragment() {

    override val layoutId: Int = R.layout.fragment_spotify_dashboard_host

    override val backStackLayoutId: Int = R.id.dashboard_back_stack_layout

    override val mainFragment: Fragment
        get() = SpotifyDashboardFragment()
}
