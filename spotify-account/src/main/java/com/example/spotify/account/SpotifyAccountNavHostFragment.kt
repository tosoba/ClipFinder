package com.example.spotify.account

import androidx.fragment.app.Fragment
import com.example.core.android.base.fragment.BaseNavHostFragment

class SpotifyAccountNavHostFragment : BaseNavHostFragment() {
    override val layoutId: Int = R.layout.fragment_spotify_account_host
    override val mainFragment: Fragment get() = SpotifyAccountFragment()
    override val backStackLayoutId: Int = R.id.spotify_account_back_stack_layout
}
