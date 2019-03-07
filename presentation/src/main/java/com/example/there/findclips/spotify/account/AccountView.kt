package com.example.there.findclips.spotify.account

import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentStatePagerAdapter

class AccountView(
        val onTabSelectedListener: TabLayout.OnTabSelectedListener,
        val pagerAdapter: FragmentStatePagerAdapter,
        val offScreenPageLimit: Int
)