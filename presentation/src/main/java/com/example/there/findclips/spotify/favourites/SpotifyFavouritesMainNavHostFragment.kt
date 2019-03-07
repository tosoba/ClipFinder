package com.example.there.findclips.spotify.favourites

import android.support.v4.app.Fragment
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseNavHostFragment


class SpotifyFavouritesMainNavHostFragment : BaseNavHostFragment() {

    override val layoutId: Int = R.layout.fragment_spotify_favourites_main_host

    override val backStackLayoutId: Int = R.id.favourites_back_stack_layout

    override val mainFragment: Fragment
        get() = SpotifyFavouritesMainFragment()
}
