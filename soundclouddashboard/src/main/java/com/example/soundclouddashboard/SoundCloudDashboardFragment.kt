package com.example.soundclouddashboard

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coreandroid.BR
import com.example.coreandroid.base.IFragmentFactory
import com.example.coreandroid.base.fragment.BaseVMFragment
import com.example.coreandroid.base.fragment.HasMainToolbar
import com.example.coreandroid.lifecycle.ConnectivityComponent
import com.example.coreandroid.model.soundcloud.SoundCloudPlaylist
import com.example.coreandroid.model.soundcloud.SoundCloudSystemPlaylist
import com.example.coreandroid.util.ext.*
import com.example.coreandroid.view.recyclerview.binder.ItemBinder
import com.example.coreandroid.view.recyclerview.binder.ItemBinderBase
import com.example.coreandroid.view.recyclerview.item.ListItemView
import com.example.coreandroid.view.recyclerview.item.RecyclerViewItemView
import com.example.coreandroid.view.recyclerview.item.RecyclerViewItemViewState
import com.example.coreandroid.view.recyclerview.listener.ClickHandler
import com.example.soundclouddashboard.databinding.FragmentSoundCloudDashboardBinding
import kotlinx.android.synthetic.main.fragment_sound_cloud_dashboard.*
import javax.inject.Inject


class SoundCloudDashboardFragment : BaseVMFragment<SoundCloudDashboardViewModel>(
        SoundCloudDashboardViewModel::class.java
), HasMainToolbar {

    @Inject
    lateinit var fragmentFactory: IFragmentFactory

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
                                get() = ItemBinderBase(BR.imageListItem, com.example.coreandroid.R.layout.named_image_list_item)
                        },
                        ClickHandler { navHostFragment?.showFragment(fragmentFactory.newSoundCloudPlaylistFragmentWithPlaylist(it), true) },
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
                                get() = ItemBinderBase(BR.imageListItem, com.example.coreandroid.R.layout.named_image_list_item)
                        },
                        ClickHandler { navHostFragment?.showFragment(fragmentFactory.newSoundCloudPlaylistFragmentWithSystemPlaylist(it), true) },
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
    ): View? = DataBindingUtil.inflate<FragmentSoundCloudDashboardBinding>(
            inflater, R.layout.fragment_sound_cloud_dashboard, container, false
    ).apply {
        dashboardView = view
        appCompatActivity?.setSupportActionBar(soundCloudDashboardToolbar)
        appCompatActivity?.showDrawerHamburger()
        soundCloudDashboardRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
    }.root

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        if (toolbar.menu?.size() == 0) appCompatActivity?.setSupportActionBar(toolbar)
    }

    override fun onOptionsItemSelected(
            item: MenuItem?
    ): Boolean = if (item?.itemId == android.R.id.home
            && parentFragment?.childFragmentManager?.backStackEntryCount == 0) {
        navigationDrawerController?.openDrawer()
        true
    } else false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(connectivityComponent)
    }

    private fun loadData() = viewModel.loadPlaylists()
}
