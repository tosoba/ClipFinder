package com.example.there.findclips.fragment.dashboard

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.*
import com.example.there.data.preferences.PreferencesHelper
import com.example.there.findclips.BR
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseSpotifyVMFragment
import com.example.there.findclips.base.fragment.HasMainToolbar
import com.example.there.findclips.databinding.FragmentDashboardBinding
import com.example.there.findclips.di.Injectable
import com.example.there.findclips.fragment.spotifyitem.album.AlbumFragment
import com.example.there.findclips.fragment.spotifyitem.category.CategoryFragment
import com.example.there.findclips.fragment.spotifyitem.playlist.PlaylistFragment
import com.example.there.findclips.fragment.trackvideos.TrackVideosFragment
import com.example.there.findclips.lifecycle.ConnectivityComponent
import com.example.there.findclips.lifecycle.DisposablesComponent
import com.example.there.findclips.model.entity.Album
import com.example.there.findclips.model.entity.Category
import com.example.there.findclips.model.entity.Playlist
import com.example.there.findclips.model.entity.TopTrack
import com.example.there.findclips.util.ext.hostFragment
import com.example.there.findclips.util.ext.mainActivity
import com.example.there.findclips.util.ext.openDrawer
import com.example.there.findclips.util.ext.showDrawerHamburger
import com.example.there.findclips.view.list.ClickHandler
import com.example.there.findclips.view.list.binder.ItemBinder
import com.example.there.findclips.view.list.binder.ItemBinderBase
import com.example.there.findclips.view.list.item.ListItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemViewState
import com.example.there.findclips.view.recycler.EndlessRecyclerOnScrollListener
import kotlinx.android.synthetic.main.fragment_dashboard.*
import javax.inject.Inject


class DashboardFragment : BaseSpotifyVMFragment<DashboardViewModel>(DashboardViewModel::class.java), Injectable, HasMainToolbar {

    override val toolbar: Toolbar
        get() = dashboard_toolbar

    private val onNewReleasesScrollListener: RecyclerView.OnScrollListener by lazy {
        object : EndlessRecyclerOnScrollListener(returnFromOnScrolledItemCount = 1) {
            override fun onLoadMore() {
                preferenceHelper.accessToken?.let {
                    viewModel.loadNewReleases(it, true)
                }
            }
        }
    }

    private val dashboardAdapter: DashboardAdapter by lazy {
        DashboardAdapter(
                RecyclerViewItemView(
                        RecyclerViewItemViewState(
                                viewModel.viewState.categoriesLoadingInProgress,
                                viewModel.viewState.categories
                        ),
                        object : ListItemView<Category>(viewModel.viewState.categories) {
                            override val itemViewBinder: ItemBinder<Category>
                                get() = ItemBinderBase(BR.category, R.layout.category_item)
                        },
                        ClickHandler {
                            hostFragment?.showFragment(CategoryFragment.newInstance(category = it), true)
                        },
                        null,
                        null,
                        View.OnClickListener { _ -> preferenceHelper.accessToken?.let { viewModel.loadCategories(it) } }
                ),
                RecyclerViewItemView(
                        RecyclerViewItemViewState(
                                viewModel.viewState.featuredPlaylistsLoadingInProgress,
                                viewModel.viewState.featuredPlaylists
                        ),
                        object : ListItemView<Playlist>(viewModel.viewState.featuredPlaylists) {
                            override val itemViewBinder: ItemBinder<Playlist>
                                get() = ItemBinderBase(BR.playlist, R.layout.playlist_item)
                        },
                        ClickHandler {
                            hostFragment?.showFragment(PlaylistFragment.newInstance(playlist = it), true)
                        },
                        null,
                        null,
                        View.OnClickListener { _ -> preferenceHelper.accessToken?.let { viewModel.loadFeaturedPlaylists(it) } }
                ),
                RecyclerViewItemView(
                        RecyclerViewItemViewState(
                                viewModel.viewState.topTracksLoadingInProgress,
                                viewModel.viewState.topTracks
                        ),
                        object : ListItemView<TopTrack>(viewModel.viewState.topTracks) {
                            override val itemViewBinder: ItemBinder<TopTrack>
                                get() = ItemBinderBase(BR.track, R.layout.top_track_item)
                        },
                        ClickHandler {
                            hostFragment?.showFragment(TrackVideosFragment.newInstance(track = it.track), true)
                        },
                        null,
                        null,
                        View.OnClickListener { _ -> preferenceHelper.accessToken?.let { viewModel.loadDailyViralTracks(it) } }
                ),
                RecyclerViewItemView(
                        RecyclerViewItemViewState(
                                viewModel.viewState.newReleasesLoadingInProgress,
                                viewModel.viewState.newReleases
                        ),
                        object : ListItemView<Album>(viewModel.viewState.newReleases) {
                            override val itemViewBinder: ItemBinder<Album>
                                get() = ItemBinderBase(BR.album, R.layout.album_item)
                        },
                        ClickHandler {
                            hostFragment?.showFragment(AlbumFragment.newInstance(album = it), true)
                        },
                        null,
                        onNewReleasesScrollListener,
                        View.OnClickListener { _ ->
                            preferenceHelper.accessToken?.let {
                                val loadMore = viewModel.viewState.newReleases.size > 0
                                viewModel.loadNewReleases(it, loadMore)
                            }
                        }
                )
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
            mainActivity?.setSupportActionBar(dashboardToolbar)
            mainActivity?.showDrawerHamburger()
            dashboardRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        if (toolbar.menu?.size() == 0) mainActivity?.setSupportActionBar(toolbar)
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
                true
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        lifecycle.addObserver(disposablesComponent)
        observePreferences()
        loadData()
    }

    override fun onOptionsItemSelected(
            item: MenuItem?
    ): Boolean = if (item?.itemId == android.R.id.home && parentFragment?.childFragmentManager?.backStackEntryCount == 0) {
        mainActivity?.openDrawer()
        true
    } else false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(connectivityComponent)
    }

    private fun loadData() = viewModel.loadDashboardData(preferenceHelper.accessToken)

    private val disposablesComponent = DisposablesComponent()

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    private fun observePreferences() {
        fun reloadDataOnPreferencesChange() = preferencesHelper.accessToken?.let {
            viewModel.loadCategories(it, true)
            viewModel.loadFeaturedPlaylists(it, true)
        }

        disposablesComponent.addAll(
                preferencesHelper.countryObservable
                        .skip(1)
                        .distinctUntilChanged()
                        .subscribe { reloadDataOnPreferencesChange() },
                preferencesHelper.languageObservable
                        .skip(1)
                        .distinctUntilChanged()
                        .subscribe { reloadDataOnPreferencesChange() }
        )
    }
}
