package com.example.there.findclips.soundcloud.dashboard

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.*
import com.example.there.findclips.R
import com.example.there.findclips.soundcloud.playlist.SoundCloudPlaylistFragment


class SoundCloudDashboardFragment : com.example.coreandroid.base.fragment.BaseVMFragment<SoundCloudDashboardViewModel>(
        SoundCloudDashboardViewModel::class.java
), com.example.coreandroid.di.Injectable, com.example.coreandroid.base.fragment.HasMainToolbar {

    override val toolbar: Toolbar
        get() = sound_cloud_dashboard_toolbar

    private val isDataLoaded: Boolean
        get() = viewModel.viewState.systemPlaylists.isNotEmpty() &&
                viewModel.viewState.playlists.isNotEmpty()

    private val connectivityComponent: ConnectivityComponent by lazy {
        ConnectivityComponent(
                activity!!,
                { isDataLoaded },
                connectivitySnackbarHost!!.connectivitySnackbarParentView!!,
                ::loadData,
                true
        )
    }

    private val dashboardAdapter: SoundCloudDashboardAdapter by lazy(LazyThreadSafetyMode.NONE) {
        SoundCloudDashboardAdapter(
                RecyclerViewItemView(
                        RecyclerViewItemViewState(
                                viewModel.viewState.loadingInProgress,
                                viewModel.viewState.playlists,
                                viewModel.viewState.loadingErrorOccurred
                        ),
                        object : ListItemView<SoundCloudPlaylist>(viewModel.viewState.playlists) {
                            override val itemViewBinder: ItemBinder<SoundCloudPlaylist>
                                get() = ItemBinderBase(BR.imageListItem, R.layout.named_image_list_item)
                        },
                        ClickHandler { navHostFragment?.showFragment(SoundCloudPlaylistFragment.newInstance(it), true) },
                        onReloadBtnClickListener = View.OnClickListener { viewModel.loadPlaylists() }
                ),
                RecyclerViewItemView(
                        RecyclerViewItemViewState(
                                viewModel.viewState.loadingInProgress,
                                viewModel.viewState.systemPlaylists,
                                viewModel.viewState.loadingErrorOccurred
                        ),
                        object : ListItemView<SoundCloudSystemPlaylist>(viewModel.viewState.systemPlaylists) {
                            override val itemViewBinder: ItemBinder<SoundCloudSystemPlaylist>
                                get() = ItemBinderBase(BR.imageListItem, R.layout.named_image_list_item)
                        },
                        ClickHandler { navHostFragment?.showFragment(SoundCloudPlaylistFragment.newInstance(it), true) },
                        onReloadBtnClickListener = View.OnClickListener { viewModel.loadPlaylists() }
                )
        )
    }

    private val view: SoundCloudDashboardView by lazy(LazyThreadSafetyMode.NONE) {
        SoundCloudDashboardView(
                state = viewModel.viewState,
                dashboardAdapter = dashboardAdapter
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        loadData()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = DataBindingUtil.inflate<com.example.there.findclips.databinding.FragmentSoundCloudDashboardBinding>(
            inflater, R.layout.fragment_sound_cloud_dashboard, container, false
    ).apply {
        dashboardView = view
        appCompatActivity?.setSupportActionBar(soundCloudDashboardToolbar)
        appCompatActivity?.showDrawerHamburger()
        soundCloudDashboardRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }.root

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        if (toolbar.menu?.size() == 0) appCompatActivity?.setSupportActionBar(toolbar)
    }

    override fun onOptionsItemSelected(
            item: MenuItem?
    ): Boolean = if (item?.itemId == android.R.id.home && parentFragment?.childFragmentManager?.backStackEntryCount == 0) {
        navigationDrawerController?.openDrawer()
        true
    } else false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(connectivityComponent)
    }

    private fun loadData() = viewModel.loadPlaylists()
}
