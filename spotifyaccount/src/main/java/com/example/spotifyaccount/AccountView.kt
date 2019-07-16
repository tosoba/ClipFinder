package com.example.spotifyaccount

import com.google.android.material.tabs.TabLayout

class AccountView(
        val onTabSelectedListener: TabLayout.OnTabSelectedListener,
        val pagerAdapter: androidx.fragment.app.FragmentStatePagerAdapter,
        val offScreenPageLimit: Int
)