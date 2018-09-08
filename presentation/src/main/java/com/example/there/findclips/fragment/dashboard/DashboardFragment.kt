package com.example.there.findclips.fragment.dashboard

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseSpotifyVMFragment
import com.example.there.findclips.databinding.FragmentDashboardBinding
import com.example.there.findclips.di.Injectable
import com.example.there.findclips.fragment.category.CategoryFragment
import com.example.there.findclips.fragment.playlist.PlaylistFragment
import com.example.there.findclips.fragment.trackvideos.TrackVideosFragment
import com.example.there.findclips.lifecycle.ConnectivityComponent
import com.example.there.findclips.model.entity.Category
import com.example.there.findclips.model.entity.Playlist
import com.example.there.findclips.model.entity.TopTrack
import com.example.there.findclips.util.ext.accessToken
import com.example.there.findclips.util.ext.hostFragment
import com.example.there.findclips.view.list.*
import kotlinx.android.synthetic.main.fragment_dashboard.*


class DashboardFragment : BaseSpotifyVMFragment<DashboardViewModel>(), Injectable {

    private val topTrackItemClickListener = object : OnTopTrackClickListener {
        override fun onClick(item: TopTrack) {
            hostFragment?.showFragment(TrackVideosFragment.newInstance(track = item.track), true)
        }
    }

    private val categoryItemClickListener = object : OnCategoryClickListener {
        override fun onClick(item: Category) {
            hostFragment?.showFragment(CategoryFragment.newInstance(category = item), true)
        }
    }

    private val playlistItemClickListener = object : OnPlaylistClickListener {
        override fun onClick(item: Playlist) {
            hostFragment?.showFragment(PlaylistFragment.newInstance(playlist = item), true)
        }
    }

    private val view: DashboardView by lazy {
        DashboardView(
                state = viewModel.viewState,
                categoriesAdapter = CategoriesList.Adapter(viewModel.viewState.categories, R.layout.category_item, categoryItemClickListener),
                playlistsAdapter = PlaylistsList.Adapter(viewModel.viewState.featuredPlaylists, R.layout.playlist_item, playlistItemClickListener),
                topTracksAdapter = TopTracksList.Adapter(viewModel.viewState.topTracks, R.layout.top_track_item, topTrackItemClickListener)
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentDashboardBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        return binding.apply {
            dashboardView = view
            genresRecyclerView.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
            featuredPlaylistsRecyclerview.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
            topTracksRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }.root
    }

    private val isDataLoaded: Boolean
        get() = viewModel.viewState.categories.isNotEmpty() &&
                viewModel.viewState.featuredPlaylists.isNotEmpty() &&
                viewModel.viewState.topTracks.isNotEmpty()

    private val connectivityComponent: ConnectivityComponent by lazy {
        ConnectivityComponent(
                activity!!,
                { isDataLoaded },
                dashboard_root_layout,
                ::loadData,
                true
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        lifecycle.addObserver(connectivityComponent)

        if (savedInstanceState == null)
            loadData()
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(DashboardViewModel::class.java)
    }

    private fun loadData() = viewModel.loadDashboardData(activity?.accessToken)
}
