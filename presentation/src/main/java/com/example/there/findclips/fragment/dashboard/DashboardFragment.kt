package com.example.there.findclips.fragment.dashboard

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.*
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseSpotifyVMFragment
import com.example.there.findclips.base.fragment.HasMainToolbar
import com.example.there.findclips.databinding.FragmentDashboardBinding
import com.example.there.findclips.di.Injectable
import com.example.there.findclips.fragment.album.AlbumFragment
import com.example.there.findclips.fragment.category.CategoryFragment
import com.example.there.findclips.fragment.playlist.PlaylistFragment
import com.example.there.findclips.fragment.trackvideos.TrackVideosFragment
import com.example.there.findclips.lifecycle.ConnectivityComponent
import com.example.there.findclips.lifecycle.DisposablesComponent
import com.example.there.findclips.util.ext.accessToken
import com.example.there.findclips.util.ext.hostFragment
import com.example.there.findclips.util.ext.mainActivity
import com.example.there.findclips.view.list.impl.AlbumsList
import com.example.there.findclips.view.list.impl.CategoriesList
import com.example.there.findclips.view.list.impl.PlaylistsList
import com.example.there.findclips.view.list.impl.TopTracksList
import com.example.there.findclips.view.list.item.RecyclerViewItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemViewState
import com.example.there.findclips.view.recycler.EndlessRecyclerOnScrollListener
import kotlinx.android.synthetic.main.fragment_dashboard.*


class DashboardFragment : BaseSpotifyVMFragment<DashboardViewModel>(), Injectable, HasMainToolbar {

    override val toolbar: Toolbar
        get() = dashboard_toolbar

    private val categoriesAdapter: CategoriesList.Adapter by lazy {
        CategoriesList.Adapter(viewModel.viewState.categories, R.layout.category_item)
    }

    private val playlistsAdapter: PlaylistsList.Adapter by lazy {
        PlaylistsList.Adapter(viewModel.viewState.featuredPlaylists, R.layout.playlist_item)
    }

    private val topTracksAdapter: TopTracksList.Adapter by lazy {
        TopTracksList.Adapter(viewModel.viewState.topTracks, R.layout.top_track_item)
    }

    private val newReleasesAdapter: AlbumsList.Adapter by lazy {
        AlbumsList.Adapter(viewModel.viewState.newReleases, R.layout.album_item)
    }

    private val onNewReleasesScrollListener: RecyclerView.OnScrollListener by lazy {
        object : EndlessRecyclerOnScrollListener(returnFromOnScrolledItemCount = 1) {
            override fun onLoadMore() {
                activity?.accessToken?.let {
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
                                viewModel.viewState.categoriesErrorOccurred
                        ),
                        categoriesAdapter,
                        null,
                        null,
                        View.OnClickListener { activity?.accessToken?.let { viewModel.loadCategories(it) } }
                ),
                RecyclerViewItemView(
                        RecyclerViewItemViewState(
                                viewModel.viewState.featuredPlaylistsLoadingInProgress,
                                viewModel.viewState.playlistsErrorOccurred
                        ),
                        playlistsAdapter,
                        null,
                        null,
                        View.OnClickListener { activity?.accessToken?.let { viewModel.loadFeaturedPlaylists(it) } }
                ),
                RecyclerViewItemView(
                        RecyclerViewItemViewState(
                                viewModel.viewState.topTracksLoadingInProgress,
                                viewModel.viewState.topTracksErrorOccurred
                        ),
                        topTracksAdapter,
                        null,
                        null,
                        View.OnClickListener { activity?.accessToken?.let { viewModel.loadDailyViralTracks(it) } }
                ),
                RecyclerViewItemView(
                        RecyclerViewItemViewState(
                                viewModel.viewState.newReleasesLoadingInProgress,
                                viewModel.viewState.newReleasesErrorOccurred
                        ),
                        newReleasesAdapter,
                        null,
                        onNewReleasesScrollListener,
                        View.OnClickListener {
                            activity?.accessToken?.let {
                                val loadMore = viewModel.viewState.newReleases.size > 0
                                val onFinally: (() -> Unit)? = if (loadMore) {
                                    {
                                        newReleasesAdapter.scrollToTop()
                                        viewBinding?.executePendingBindings()
                                    }
                                } else null
                                viewModel.loadNewReleases(it, loadMore, onFinally)
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

    private var viewBinding: FragmentDashboardBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        return viewBinding!!.apply {
            dashboardView = view
            mainActivity?.setSupportActionBar(dashboardToolbar)
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
                false
        )
    }

    private val disposablesComponent = DisposablesComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        lifecycle.addObserver(disposablesComponent)
        initItemClicks()

        loadData()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = false

    private fun initItemClicks() = disposablesComponent.addAll(
            categoriesAdapter.itemClicked.subscribe {
                hostFragment?.showFragment(CategoryFragment.newInstance(category = it), true)
            },
            playlistsAdapter.itemClicked.subscribe {
                hostFragment?.showFragment(PlaylistFragment.newInstance(playlist = it), true)
            },
            topTracksAdapter.itemClicked.subscribe {
                hostFragment?.showFragment(TrackVideosFragment.newInstance(track = it.track), true)
            },
            newReleasesAdapter.itemClicked.subscribe {
                hostFragment?.showFragment(AlbumFragment.newInstance(album = it), true)
            }
    )


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(connectivityComponent)
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(DashboardViewModel::class.java)
    }

    private fun loadData() = viewModel.loadDashboardData(activity?.accessToken) {
        newReleasesAdapter.scrollToTop()
        viewBinding?.executePendingBindings()
    }
}
