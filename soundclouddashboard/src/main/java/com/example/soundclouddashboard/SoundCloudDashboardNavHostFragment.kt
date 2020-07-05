package com.example.soundclouddashboard

import androidx.fragment.app.Fragment
import com.example.coreandroid.base.fragment.BaseNavHostFragment

class SoundCloudDashboardNavHostFragment : BaseNavHostFragment() {
    override val backStackLayoutId: Int = R.id.sound_cloud_dashboard_back_stack_layout
    override val layoutId: Int = R.layout.fragment_sound_cloud_dashboard_nav_host
    override val mainFragment: Fragment get() = SoundCloudDashboardFragment()
}
