package com.example.there.findclips.soundcloud.dashboard

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.*
import com.example.there.findclips.BR
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseVMFragment
import com.example.there.findclips.base.fragment.HasMainToolbar
import com.example.there.findclips.di.Injectable
import com.example.there.findclips.lifecycle.ConnectivityComponent
import com.example.there.findclips.model.entity.soundcloud.SoundCloudPlaylist
import com.example.there.findclips.model.entity.soundcloud.SoundCloudSystemPlaylist
import com.example.there.findclips.util.ext.appCompatActivity
import com.example.there.findclips.util.ext.connectivitySnackbarHost
import com.example.there.findclips.util.ext.navigationDrawerController
import com.example.there.findclips.util.ext.showDrawerHamburger
import com.example.there.findclips.view.list.ClickHandler
import com.example.there.findclips.view.list.binder.ItemBinder
import com.example.there.findclips.view.list.binder.ItemBinderBase
import com.example.there.findclips.view.list.item.ListItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemViewState
import kotlinx.android.synthetic.main.fragment_sound_cloud_dashboard.*


class SoundCloudDashboardFragment : BaseVMFragment<SoundCloudDashboardViewModel>(
        SoundCloudDashboardViewModel::class.java
), Injectable, HasMainToolbar {

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
                        ClickHandler { },
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
                        ClickHandler { },
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
