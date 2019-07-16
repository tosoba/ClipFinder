package com.example.soundcloudfavourites

import com.example.coreandroid.base.fragment.BaseNavHostFragment


class SoundCloudFavouritesNavHostFragment : BaseNavHostFragment() {

    override val backStackLayoutId: Int = R.id.sound_cloud_favourites_back_stack_layout

    override val layoutId: Int = R.layout.fragment_sound_cloud_favourites_nav_host

    override val mainFragment: androidx.fragment.app.Fragment
        get() = SoundCloudFavouritesFragment()
}
