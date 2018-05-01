package com.example.there.findclips.dashboard

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
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

    private val topTrackItemClickListener = object : TopTracksList.OnItemClickListener {
        override fun onClick(item: TopTrackEntity) {
            MainRouter.goToSearchFragmentWithVideosQuery(mainActivity, query = item.track.query)
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentDashboardBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        binding.viewState = mainViewModel.viewState
        binding.categoriesAdapter = CategoriesList.Adapter(mainViewModel.viewState.categories, R.layout.category_item, categoryItemClickListener)
        binding.playlistsAdapter = PlaylistsList.Adapter(mainViewModel.viewState.featuredPlaylists, R.layout.playlist_item, playlistItemClickListener)
        binding.topTracksAdapter = TopTracksList.Adapter(mainViewModel.viewState.topTracks, R.layout.top_track_item, topTrackItemClickListener)
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
