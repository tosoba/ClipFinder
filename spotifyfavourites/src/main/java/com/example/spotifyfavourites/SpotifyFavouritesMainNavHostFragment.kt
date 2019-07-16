package com.example.spotifyfavourites

import com.example.coreandroid.base.fragment.BaseNavHostFragment

class SpotifyFavouritesMainNavHostFragment : BaseNavHostFragment() {

    override val layoutId: Int = R.layout.fragment_spotify_favourites_main_host

    override val backStackLayoutId: Int = R.id.favourites_back_stack_layout

    override val mainFragment: androidx.fragment.app.Fragment
        get() = SpotifyFavouritesMainFragment()
}