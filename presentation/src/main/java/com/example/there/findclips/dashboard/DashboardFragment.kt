package com.example.there.findclips.dashboard

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.domain.entities.spotify.CategoryEntity
import com.example.there.domain.entities.spotify.PlaylistEntity
import com.example.there.domain.entities.spotify.TopTrackEntity
import com.example.there.findclips.R
import com.example.there.findclips.base.BaseSpotifyVMFragment
import com.example.there.findclips.databinding.FragmentDashboardBinding
import com.example.there.findclips.lists.*
import com.example.there.findclips.main.MainFragment
import com.example.there.findclips.util.accessToken
import com.example.there.findclips.util.app
import javax.inject.Inject


class DashboardFragment : BaseSpotifyVMFragment<DashboardViewModel>(), MainFragment {

    override val title: String
        get() = "Dashboard"

    override val bottomNavigationItemId: Int
        get() = R.id.action_dashboard

    @Inject
    lateinit var vmFactory: DashboardVMFactory

    private val topTrackItemClickListener = object : TopTracksList.OnItemClickListener {
        override fun onClick(item: TopTrackEntity) {

        }
    }

    private val categoryItemClickListener = object : CategoriesList.OnItemClickListener {
        override fun onClick(item: CategoryEntity) {

        }
    }

    private val playlistItemClickListener = object : PlaylistsList.OnItemClickListener {
        override fun onClick(item: PlaylistEntity) {

        }
    }

    private val view: DashboardFragmentView by lazy {
        DashboardFragmentView(
                state = mainViewModel.viewState,
                categoriesAdapter = CategoriesList.Adapter(mainViewModel.viewState.categories, R.layout.category_item, categoryItemClickListener),
                categoriesLayoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false),
                playlistsAdapter = PlaylistsList.Adapter(mainViewModel.viewState.featuredPlaylists, R.layout.playlist_item, playlistItemClickListener),
                playlistsLayoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false),
                topTracksAdapter = TopTracksList.Adapter(mainViewModel.viewState.topTracks, R.layout.top_track_item, topTrackItemClickListener),
                topTracksLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentDashboardBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        binding.dashboardFragmentView = view
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
