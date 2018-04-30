package com.example.there.findclips.dashboard

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.domain.entities.spotify.TopTrackEntity
import com.example.there.findclips.R
import com.example.there.findclips.base.BaseSpotifyVMFragment
import com.example.there.findclips.dashboard.lists.toptracks.TopTrackItemClickListener
import com.example.there.findclips.databinding.FragmentDashboardBinding
import com.example.there.findclips.main.MainFragment
import com.example.there.findclips.main.MainRouter
import com.example.there.findclips.util.accessToken
import com.example.there.findclips.util.app
import com.example.there.findclips.util.mainActivity
import javax.inject.Inject


class DashboardFragment : BaseSpotifyVMFragment<DashboardViewModel>(), MainFragment {

    override val bottomNavigationItemId: Int
        get() = R.id.action_dashboard

    @Inject
    lateinit var vmFactory: DashboardVMFactory

    private val onTopTrackClickListener = object : TopTrackItemClickListener {
        override fun onClick(track: TopTrackEntity) {
            MainRouter.goToSearchFragmentWithVideosQuery(mainActivity, query = track.track.query)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentDashboardBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        binding.viewState = mainViewModel.viewState
        binding.onTopTrackClickListener = onTopTrackClickListener
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState == null) {
            mainViewModel.loadDashboardData(activity?.accessToken)
        }
    }

    override fun initViewModel() {
        mainViewModel = ViewModelProviders.of(this, vmFactory).get(DashboardViewModel::class.java)
    }

    override fun initComponent() {
        activity?.app?.createDashboardComponent()?.inject(this)
    }

    override fun releaseComponent() {
        activity?.app?.releaseDashboardComponent()
    }
}
