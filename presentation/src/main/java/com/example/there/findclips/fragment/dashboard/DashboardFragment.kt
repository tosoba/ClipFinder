package com.example.there.findclips.fragment.dashboard

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
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
import com.example.there.findclips.lifecycle.DisposablesComponent
import com.example.there.findclips.util.ext.accessToken
import com.example.there.findclips.util.ext.hostFragment
import com.example.there.findclips.util.ext.mainActivity
import com.example.there.findclips.view.list.impl.CategoriesList
import com.example.there.findclips.view.list.impl.PlaylistsList
import com.example.there.findclips.view.list.impl.TopTracksList


class DashboardFragment : BaseSpotifyVMFragment<DashboardViewModel>(), Injectable {

    private val categoriesAdapter: CategoriesList.Adapter by lazy {
        CategoriesList.Adapter(viewModel.viewState.categories, R.layout.category_item)
    }

    private val playlistsAdapter: PlaylistsList.Adapter by lazy {
        PlaylistsList.Adapter(viewModel.viewState.featuredPlaylists, R.layout.playlist_item)
    }

    private val topTracksAdapter: TopTracksList.Adapter by lazy {
        TopTracksList.Adapter(viewModel.viewState.topTracks, R.layout.top_track_item)
    }

    private val dashboardAdapter: DashboardAdapter by lazy {
        DashboardAdapter(
                categoriesAdapter,
                playlistsAdapter,
                topTracksAdapter,
                viewModel.viewState.categoriesLoadingInProgress,
                viewModel.viewState.featuredPlaylistsLoadingInProgress,
                viewModel.viewState.topTracksLoadingInProgress
        )
    }

    private val view: DashboardView by lazy {
        DashboardView(
                state = viewModel.viewState,
                dashboardAdapter = dashboardAdapter
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentDashboardBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        return binding.apply {
            dashboardView = view
            dashboardRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
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
                mainActivity!!.connectivitySnackbarParentView!!,
                ::loadData,
                false
        )
    }

    private val disposablesComponent = DisposablesComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(disposablesComponent)
        initItemClicks()

        loadData()
    }

    private fun initItemClicks() {
        disposablesComponent.add(categoriesAdapter.itemClicked.subscribe {
            hostFragment?.showFragment(CategoryFragment.newInstance(category = it), true)
        })
        disposablesComponent.add(playlistsAdapter.itemClicked.subscribe {
            hostFragment?.showFragment(PlaylistFragment.newInstance(playlist = it), true)
        })
        disposablesComponent.add(topTracksAdapter.itemClicked.subscribe {
            hostFragment?.showFragment(TrackVideosFragment.newInstance(track = it.track), true)
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(connectivityComponent)
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(DashboardViewModel::class.java)
    }

    private fun loadData() = viewModel.loadDashboardData(activity?.accessToken)
}
