package com.example.spotifydashboard.ui

import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.example.core.ext.castAs
import com.example.coreandroid.*
import com.example.coreandroid.base.IFragmentFactory
import com.example.coreandroid.base.fragment.HasMainToolbar
import com.example.coreandroid.base.handler.NavigationDrawerController
import com.example.coreandroid.di.EpoxyHandlerQualifier
import com.example.coreandroid.model.LoadedSuccessfully
import com.example.coreandroid.model.Loading
import com.example.coreandroid.model.LoadingFailed
import com.example.coreandroid.model.spotify.clickableListItem
import com.example.coreandroid.util.carousel
import com.example.coreandroid.util.ext.NavigationCapable
import com.example.coreandroid.util.ext.mainContentFragment
import com.example.coreandroid.util.ext.show
import com.example.coreandroid.util.ext.showDrawerHamburger
import com.example.coreandroid.util.infiniteCarousel
import com.example.coreandroid.util.typedController
import com.example.coreandroid.util.withModelsFrom
import com.example.coreandroid.view.epoxy.Column
import com.example.spotifydashboard.R
import com.example.spotifydashboard.databinding.FragmentSpotifyDashboardBinding
import kotlinx.android.synthetic.main.fragment_spotify_dashboard.*
import org.koin.android.ext.android.inject

class SpotifyDashboardFragment : BaseMvRxFragment(), HasMainToolbar, NavigationCapable {

    override val factory: IFragmentFactory by inject()

    private val builder by inject<Handler>(EpoxyHandlerQualifier.BUILDER)
    private val differ by inject<Handler>(EpoxyHandlerQualifier.DIFFER)

    private val viewModel: SpotifyDashboardViewModel by fragmentViewModel()

    override val toolbar: Toolbar get() = dashboard_toolbar

    private val epoxyController by lazy(LazyThreadSafetyMode.NONE) {
        typedController(builder, differ, viewModel) { state ->
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = DataBindingUtil.inflate<FragmentSpotifyDashboardBinding>(
        inflater, R.layout.fragment_spotify_dashboard, container, false
    ).apply {
        requireActivity().castAs<AppCompatActivity>()?.apply {
            setSupportActionBar(dashboardToolbar)
            showDrawerHamburger()
        }
        mainContentFragment?.disablePlayButton()
        dashboardRecyclerView.apply {
            setController(epoxyController)
            //TODO: animation
        }
    }.root

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        if (toolbar.menu?.size() == 0) {
            requireActivity().castAs<AppCompatActivity>()?.setSupportActionBar(toolbar)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home
            && parentFragment?.childFragmentManager?.backStackEntryCount == 0) {
            activity?.castAs<NavigationDrawerController>()?.openDrawer()
            true
        } else false
    }

    override fun invalidate() {
        withState(viewModel) { state -> epoxyController.setData(state) }
    }
}
