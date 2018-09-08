package com.example.there.findclips.fragment.dashboard


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseHostFragment


class DashboardHostFragment : BaseHostFragment() {

    override val backStackLayoutId: Int = R.id.dashboard_back_stack_layout

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_dashboard_host, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showFragment(DashboardFragment(), false)
    }
}
