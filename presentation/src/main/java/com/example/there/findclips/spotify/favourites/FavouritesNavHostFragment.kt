package com.example.there.findclips.spotify.favourites

import android.support.v4.app.Fragment
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseNavHostFragment


class FavouritesNavHostFragment : BaseNavHostFragment() {

    override val layoutId: Int = R.layout.fragment_favourites_host

    override val backStackLayoutId: Int = R.id.favourites_back_stack_layout

    override val mainFragment: Fragment
        get() = FavouritesFragment()
}
