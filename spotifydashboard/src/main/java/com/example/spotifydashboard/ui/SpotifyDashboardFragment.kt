package com.example.spotifydashboard.ui

import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.example.coreandroid.*
import com.example.coreandroid.base.IFragmentFactory
import com.example.coreandroid.base.fragment.HasMainToolbar
import com.example.coreandroid.lifecycle.ConnectivityComponent
import com.example.coreandroid.lifecycle.DisposablesComponent
import com.example.coreandroid.model.LoadedSuccessfully
import com.example.coreandroid.model.Loading
import com.example.coreandroid.model.LoadingFailed
import com.example.coreandroid.model.spotify.clickableListItem
import com.example.coreandroid.preferences.SpotifyPreferences
import com.example.coreandroid.util.asyncController
import com.example.coreandroid.util.carousel
import com.example.coreandroid.util.ext.*
import com.example.coreandroid.util.infiniteCarousel
import com.example.coreandroid.util.withModelsFrom
import com.example.coreandroid.view.epoxy.Column
import com.example.spotifydashboard.R
import com.example.spotifydashboard.databinding.FragmentSpotifyDashboardBinding
import kotlinx.android.synthetic.main.fragment_spotify_dashboard.*
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named


class SpotifyDashboardFragment : BaseMvRxFragment(), HasMainToolbar, NavigationCapable {

    override val factory: IFragmentFactory by inject()

    private val builder by inject<Handler>(named("builder"))
    private val differ by inject<Handler>(named("differ"))

    private val viewModel: SpotifyDashboardViewModel by fragmentViewModel()

    override val toolbar: Toolbar
        get() = dashboard_toolbar

    private val epoxyController by lazy {
        asyncController(builder, differ, viewModel) { state ->
            headerItem {
                id("categories-header")
                text("Categories")
            }

            when (state.categories.status) {
                is Loading -> loadingIndicator {
                    id("loading-indicator-categories")
                }

                is LoadingFailed<*> -> reloadControl {
                    id("reload-control")
                    onReloadClicked(View.OnClickListener { viewModel.loadCategories() })
                    message("Error occurred lmao") //TODO: error msg
                }

                is LoadedSuccessfully -> carousel {
                    id("categories")
                    withModelsFrom(state.categories.value.chunked(2)) { chunk ->
                        Column(chunk.map { category ->
                            category.clickableListItem {
                                show { newSpotifyCategoryFragment(category) }
                            }
                        })
                    }
                }
            }

            headerItem {
                id("playlists-header")
                text("Featured playlists")
            }

            when (state.featuredPlaylists.status) {
                is Loading -> loadingIndicator {
                    id("loading-indicator-playlists")
                }


                is LoadingFailed<*> -> reloadControl {
                    id("reload-control")
                    onReloadClicked(View.OnClickListener { viewModel.loadFeaturedPlaylists() })
                    message("Error occurred lmao") //TODO: error msg
                }


                is LoadedSuccessfully -> carousel {
                    id("playlists")
                    withModelsFrom(state.featuredPlaylists.value.chunked(2)) { chunk ->
                        Column(chunk.map { playlist ->
                            playlist.clickableListItem {
                                show { newSpotifyPlaylistFragment(playlist) }
                            }
                        })
                    }
                }

            }

            headerItem {
                id("releases-header")
                text("New releases")
            }

            fun newReleasesCarousel(extraModels: Collection<EpoxyModel<*>>) = infiniteCarousel(
                    minItemsBeforeLoadingMore = 1,
                    onLoadMore = viewModel::loadNewReleases
            ) {
                id("releases")
                withModelsFrom(
                        items = state.newReleases.value.chunked(2),
                        extraModels = extraModels
                ) { chunk ->
                    Column(chunk.map { album ->
                        album.clickableListItem {
                            show { newSpotifyAlbumFragment(album) }
                        }
                    })
                }
            }

            if (state.newReleases.value.isEmpty()) {
                when (state.newReleases.status) {
                    is Loading -> loadingIndicator {
                        id("loading-indicator-releases")
                    }

                    is LoadingFailed<*> -> reloadControl {
                        id("reload-control")
                        onReloadClicked(View.OnClickListener { viewModel.loadNewReleases() })
                        message("Error occurred lmao") //TODO: error msg
                    }
                }
            } else {
                newReleasesCarousel(extraModels = when (state.newReleases.status) {
                    is Loading -> listOf(LoadingIndicatorBindingModel_()
                            .id("loading-more-releases"))
                    is LoadingFailed<*> -> listOf(ReloadControlBindingModel_()
                            .message("Error occurred")
                            .onReloadClicked(View.OnClickListener { viewModel.loadNewReleases() }))
                    else -> emptyList()
                })
            }


            headerItem {
                id("tracks-header")
                text("Top tracks")
            }

            when (state.topTracks.status) {
                is Loading -> loadingIndicator {
                    id("loading-indicator-tracks")
                }


                is LoadingFailed<*> -> reloadControl {
                    id("reload-control")
                    onReloadClicked(View.OnClickListener { viewModel.loadDailyViralTracks() })
                    message("Error occurred lmao") //TODO: error msg
                }


                is LoadedSuccessfully -> carousel {
                    id("tracks")
                    withModelsFrom(state.topTracks.value) { topTrack ->
                        TopTrackItemBindingModel_()
                                .id(topTrack.track.id)
                                .track(topTrack)
                                .itemClicked(View.OnClickListener {
                                    show { newSpotifyTrackVideosFragment(topTrack.track) }
                                })
                    }
                }
            }
        }
    }

    private val connectivityComponent: ConnectivityComponent by lazy {
        reloadingConnectivityComponent({
            withState(viewModel) {
                if (it.categories.loadingFailed) viewModel.loadCategories()
                if (it.featuredPlaylists.loadingFailed) viewModel.loadFeaturedPlaylists()
                if (it.topTracks.loadingFailed) viewModel.loadDailyViralTracks()
                if (it.newReleases.value.isEmpty() && it.newReleases.loadingFailed)
                    viewModel.loadNewReleases()
            }
        }) {
            withState(viewModel) {
                it.categories.loadingFailed
                        || it.featuredPlaylists.loadingFailed
                        || it.topTracks.loadingFailed
                        || (it.newReleases.value.isEmpty() && it.newReleases.loadingFailed)
            }
        }
    }

    private val disposablesComponent = DisposablesComponent()

    private val appPreferences: SpotifyPreferences by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        lifecycle.addObserver(disposablesComponent)
        observePreferences()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = DataBindingUtil.inflate<FragmentSpotifyDashboardBinding>(
            inflater, R.layout.fragment_spotify_dashboard, container, false
    ).apply {
        appCompatActivity?.setSupportActionBar(dashboardToolbar)
        appCompatActivity?.showDrawerHamburger()
        mainContentFragment?.disablePlayButton()
        dashboardRecyclerView.apply {
            setController(epoxyController)
            //TODO: animation
        }
    }.root

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        if (toolbar.menu?.size() == 0) appCompatActivity?.setSupportActionBar(toolbar)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = if (item?.itemId == android.R.id.home
            && parentFragment?.childFragmentManager?.backStackEntryCount == 0) {
        navigationDrawerController?.openDrawer()
        true
    } else false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(connectivityComponent)
    }

    override fun invalidate() {
        withState(viewModel) { state -> epoxyController.setData(state) }
    }

    private fun observePreferences() {
        fun reloadDataOnPreferencesChange() {
            viewModel.loadCategories()
            viewModel.loadFeaturedPlaylists()
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
