package com.example.there.findclips.dashboard


import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.base.BaseMainFragment
import com.example.there.findclips.databinding.FragmentDashboardBinding
import com.example.there.findclips.util.accessToken
import com.example.there.findclips.util.app
import javax.inject.Inject


class DashboardFragment : BaseMainFragment<DashboardViewModel>() {

    @Inject
    lateinit var viewModelFactory: DashboardViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentDashboardBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        binding.viewState = viewModel.viewState
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.loadDashboardData(activity?.accessToken)
        }
    }

    override fun initComponent() {
        activity?.app?.createDashboardComponent()?.inject(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DashboardViewModel::class.java)
    }

    override fun releaseComponent() {
        activity?.app?.releaseDashboardComponent()
    }
}
