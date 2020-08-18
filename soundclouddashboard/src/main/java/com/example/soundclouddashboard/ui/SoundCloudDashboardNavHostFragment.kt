package com.example.soundclouddashboard.ui

import androidx.fragment.app.Fragment
import com.example.core.android.base.fragment.BaseNavHostFragment
import com.example.soundclouddashboard.R

class SoundCloudDashboardNavHostFragment : BaseNavHostFragment() {
    override val backStackLayoutId: Int = R.id.sound_cloud_dashboard_back_stack_layout
    override val layoutId: Int = R.layout.fragment_sound_cloud_dashboard_nav_host
    override val mainFragment: Fragment get() = SoundCloudDashboardFragment()
}
