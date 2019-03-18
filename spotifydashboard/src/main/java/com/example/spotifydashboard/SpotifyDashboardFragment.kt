package com.example.spotifydashboard

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.*
import com.example.coreandroid.lifecycle.ConnectivityComponent
import com.example.coreandroid.lifecycle.DisposablesComponent
import com.example.coreandroid.model.spotify.Album
import com.example.coreandroid.model.spotify.Category
import com.example.coreandroid.model.spotify.Playlist
import com.example.coreandroid.model.spotify.TopTrack
import com.example.coreandroid.util.ext.*
import com.example.coreandroid.view.recyclerview.binder.ItemBinder
import com.example.coreandroid.view.recyclerview.binder.ItemBinderBase
import com.example.coreandroid.view.recyclerview.item.ListItemView
import com.example.coreandroid.view.recyclerview.item.RecyclerViewItemView
import com.example.coreandroid.view.recyclerview.item.RecyclerViewItemViewState
import com.example.coreandroid.view.recyclerview.listener.ClickHandler
import com.example.coreandroid.view.recyclerview.listener.EndlessRecyclerOnScrollListener
import com.example.spotifyrepo.preferences.SpotifyPreferences
import kotlinx.android.synthetic.main.fragment_spotify_dashboard.*
import javax.inject.Inject

class SpotifyDashboardFragment : com.example.coreandroid.base.fragment.BaseVMFragment<com.example.spotifydashboard.SpotifyDashboardViewModel>(
        com.example.spotifydashboard.SpotifyDashboardViewModel::class.java
), com.example.coreandroid.di.Injectable, com.example.coreandroid.base.fragment.HasMainToolbar {

    override val toolbar: Toolbar
        get() = dashboard_toolbar

    private val onNewReleasesScrollListener: RecyclerView.OnScrollListener by lazy {
        object : EndlessRecyclerOnScrollListener(returnFromOnScrolledItemCount = 1) {
            override fun onLoadMore() = viewModel.loadNewReleases(true)
        }
    }

    private val dashboardAdapter: SpotifyDashboardAdapter by lazy {
        SpotifyDashboardAdapter(
                RecyclerViewItemView(
                        RecyclerViewItemViewState(
                                viewModel.viewState.categoriesLoadingInProgress,
                                viewModel.viewState.categories,
                                viewModel.viewState.categoriesLoadingErrorOccurred
                        ),
                        object : ListItemView<Category>(viewModel.viewState.categories) {
                            override val itemViewBinder: ItemBinder<Category>
                                get() = ItemBinderBase(BR.imageListItem, R.layout.named_image_list_item)
                        },
                        ClickHandler {
                            navHostFragment?.showFragment(CategoryFragment.newInstance(category = it), true)
                        },
                        onReloadBtnClickListener = View.OnClickListener { viewModel.loadCategories() }
                ),
                RecyclerViewItemView(
                        RecyclerViewItemViewState(
                                viewModel.viewState.featuredPlaylistsLoadingInProgress,
                                viewModel.viewState.featuredPlaylists,
                                viewModel.viewState.featuredPlaylistsLoadingErrorOccurred
                        ),
                        object : ListItemView<Playlist>(viewModel.viewState.featuredPlaylists) {
                            override val itemViewBinder: ItemBinder<Playlist>
                                get() = ItemBinderBase(BR.playlist, R.layout.playlist_item)
                        },
                        ClickHandler {
                            navHostFragment?.showFragment(PlaylistFragment.newInstance(playlist = it), true)
                        },
                        onReloadBtnClickListener = View.OnClickListener { viewModel.loadFeaturedPlaylists() }
                ),
                RecyclerViewItemView(
                        RecyclerViewItemViewState(
                                viewModel.viewState.topTracksLoadingInProgress,
                                viewModel.viewState.topTracks,
                                viewModel.viewState.topTracksLoadingErrorOccurred
                        ),
                        object : ListItemView<TopTrack>(viewModel.viewState.topTracks) {
                            override val itemViewBinder: ItemBinder<TopTrack>
                                get() = ItemBinderBase(BR.track, R.layout.top_track_item)
                        },
                        ClickHandler {
                            navHostFragment?.showFragment(TrackVideosFragment.newInstance(track = it.track), true)
                        },
                        onReloadBtnClickListener = View.OnClickListener { viewModel.loadDailyViralTracks() }
                ),
                RecyclerViewItemView(
                        RecyclerViewItemViewState(
                                viewModel.viewState.newReleasesLoadingInProgress,
                                viewModel.viewState.newReleases,
                                viewModel.viewState.newReleasesLoadingErrorOccurred
                        ),
                        object : ListItemView<Album>(viewModel.viewState.newReleases) {
                            override val itemViewBinder: ItemBinder<Album>
                                get() = ItemBinderBase(BR.imageListItem, R.layout.named_image_list_item)
                        },
                        ClickHandler {
                            navHostFragment?.showFragment(AlbumFragment.newInstance(album = it), true)
                        },
                        onScrollListener = onNewReleasesScrollListener,
                        onReloadBtnClickListener = View.OnClickListener {
                            val loadMore = viewModel.viewState.newReleases.size > 0
                            viewModel.loadNewReleases(loadMore)
                        }
                )
        )
    }

    private val view: com.example.spotifydashboard.SpotifyDashboardView by lazy {
        com.example.spotifydashboard.SpotifyDashboardView(
                state = viewModel.viewState,
                dashboardAdapter = dashboardAdapter
        )
    }

    private val isDataLoaded: Boolean
        get() = viewModel.viewState.categories.isNotEmpty() &&
                viewModel.viewState.featuredPlaylists.isNotEmpty() &&
                viewModel.viewState.topTracks.isNotEmpty()

    private val connectivityComponent: ConnectivityComponent by lazy {
        ConnectivityComponent(
                activity!!,
                { isDataLoaded },
                connectivitySnackbarHost!!.connectivitySnackbarParentView!!,
                ::loadData,
                true
        )
    }

    private val disposablesComponent = DisposablesComponent()

    @Inject
    lateinit var appPreferences: SpotifyPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        lifecycle.addObserver(disposablesComponent)
        observePreferences()
        loadData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentSpotifyDashboardBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_spotify_dashboard, container, false)
        return binding.apply {
            dashboardView = view
            appCompatActivity?.setSupportActionBar(dashboardToolbar)
            appCompatActivity?.showDrawerHamburger()
            dashboardRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }.root
    }

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

    private fun loadData() = viewModel.loadData()

    private fun observePreferences() {
        fun reloadDataOnPreferencesChange() {
            viewModel.loadCategories(true)
            viewModel.loadFeaturedPlaylists(true)
        }

        disposablesComponent.addAll(
                appPreferences.countryObservable
                        .skip(1)
                        .distinctUntilChanged()
                        .subscribe { reloadDataOnPreferencesChange() },
                appPreferences.languageObservable
                        .skip(1)
                        .distinctUntilChanged()
                        .subscribe { reloadDataOnPreferencesChange() }
        )
    }
}
