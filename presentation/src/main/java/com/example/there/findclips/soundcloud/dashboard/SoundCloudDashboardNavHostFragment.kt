package com.example.there.findclips.soundcloud.dashboard


import android.support.v4.app.Fragment
import com.example.there.findclips.R


class SoundCloudDashboardNavHostFragment : com.example.coreandroid.base.fragment.BaseNavHostFragment() {

    override val backStackLayoutId: Int = R.id.sound_cloud_dashboard_back_stack_layout

    override val layoutId: Int = R.layout.fragment_sound_cloud_dashboard_nav_host

    override val mainFragment: Fragment
        get() = SoundCloudDashboardFragment()
}
