package com.example.there.findclips.dashboard

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.base.BaseSpotifyVMFragment
import com.example.there.findclips.databinding.FragmentDashboardBinding
import com.example.there.findclips.entities.Category
import com.example.there.findclips.entities.Playlist
import com.example.there.findclips.entities.TopTrack
import com.example.there.findclips.lists.*
import com.example.there.findclips.main.MainFragment
import com.example.there.findclips.Router
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
        override fun onClick(item: TopTrack) {
            Router.goToTrackVideosActivity(mainActivity, track = item.track)
        }
    }

    private val categoryItemClickListener = object : CategoriesList.OnItemClickListener {
        override fun onClick(item: Category) {
            Router.goToCategoryActivity(mainActivity, category = item)
        }
    }

    private val playlistItemClickListener = object : PlaylistsList.OnItemClickListener {
        override fun onClick(item: Playlist) {
            Router.goToPlaylistActivity(mainActivity, playlist = item)
        }
    }

    private val view: DashboardFragmentView by lazy {
        DashboardFragmentView(
                state = viewModel.viewState,
                categoriesAdapter = CategoriesList.Adapter(viewModel.viewState.categories, R.layout.category_item, categoryItemClickListener),
                playlistsAdapter = PlaylistsList.Adapter(viewModel.viewState.featuredPlaylists, R.layout.playlist_item, playlistItemClickListener),
                topTracksAdapter = TopTracksList.Adapter(viewModel.viewState.topTracks, R.layout.top_track_item, topTrackItemClickListener)
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentDashboardBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        binding.dashboardFragmentView = view

        binding.genresRecyclerView.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
        binding.featuredPlaylistsRecyclerview.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
        binding.topTracksRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.loadDashboardData(activity?.accessToken)
        }
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, vmFactory).get(DashboardViewModel::class.java)
    }

    override fun initComponent() {
        activity?.app?.createDashboardComponent()?.inject(this)
    }

    override fun releaseComponent() {
        activity?.app?.releaseDashboardComponent()
    }
}
