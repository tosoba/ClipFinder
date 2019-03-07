package com.example.there.findclips.spotify.dashboard


import android.support.v4.app.Fragment
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseNavHostFragment


class DashboardNavHostFragment : BaseNavHostFragment() {

    override val layoutId: Int = R.layout.fragment_dashboard_host

    override val backStackLayoutId: Int = R.id.dashboard_back_stack_layout

    override val mainFragment: Fragment
        get() = DashboardFragment()
}
