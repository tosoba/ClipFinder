package com.example.soundcloudfavourites


import android.support.v4.app.Fragment


class SoundCloudFavouritesNavHostFragment : com.example.coreandroid.base.fragment.BaseNavHostFragment() {

    override val backStackLayoutId: Int = R.id.sound_cloud_favourites_back_stack_layout

    override val layoutId: Int = R.layout.fragment_sound_cloud_favourites_nav_host

    override val mainFragment: Fragment
        get() = SoundCloudFavouritesFragment()
}
