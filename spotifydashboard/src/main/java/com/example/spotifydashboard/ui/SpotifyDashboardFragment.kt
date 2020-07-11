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
import com.example.coreandroid.model.Loading
import com.example.coreandroid.model.LoadingFailed
import com.example.coreandroid.model.PagedDataList
import com.example.coreandroid.model.spotify.clickableListItem
import com.example.coreandroid.util.carousel
import com.example.coreandroid.util.ext.NavigationCapable
import com.example.coreandroid.util.ext.mainContentFragment
import com.example.coreandroid.util.ext.show
import com.example.coreandroid.util.ext.showDrawerHamburger
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
            val (categories, playlists, topTracks, newReleases) = state

            fun <Value> pagedDataListCarousel(
                data: PagedDataList<Value>,
                idSuffix: String,
                loadItems: () -> Unit,
                buildItem: (Value) -> EpoxyModel<*>
            ) {
                val (value, status) = data
                if (value.isEmpty()) when (status) {
                    is Loading -> loadingIndicator {
                        id("loading-indicator-$idSuffix")
                    }

                    is LoadingFailed<*> -> reloadControl {
                        id("reload-control-$idSuffix")
                        onReloadClicked(View.OnClickListener { loadItems() })
                        message("Error occurred")
                    }
                } else carousel {
                    id(idSuffix)
                    withModelsFrom<List<Value>>(
                        items = value.chunked(2),
                        extraModels = when (status) {
                            is LoadingFailed<*> -> listOf(
                                ReloadControlBindingModel_()
                                    .id("reload-control-$idSuffix")
                                    .message("Error occurred")
                                    .onReloadClicked(View.OnClickListener { loadItems() })
                            )

                            else -> if (data.canLoadMore) listOf(
                                LoadingIndicatorBindingModel_()
                                    .id("loading-more-$idSuffix")
                                    .onBind { _, _, _ -> loadItems() }
                            ) else emptyList()
                        }
                    ) { chunk ->
                        Column(chunk.map(buildItem))
                    }
                }
            }

            headerItem {
                id("categories-header")
                text("Categories")
            }

            pagedDataListCarousel(
                categories,
                "categories",
                viewModel::loadCategories
            ) { category ->
                category.clickableListItem {
                    show { newSpotifyCategoryFragment(category) }
                }
            }

            headerItem {
                id("playlists-header")
                text("Featured playlists")
            }

            pagedDataListCarousel(
                playlists,
                "playlists",
                viewModel::loadFeaturedPlaylists
            ) { playlist ->
                playlist.clickableListItem {
                    show { newSpotifyPlaylistFragment(playlist) }
                }
            }

            headerItem {
                id("releases-header")
                text("New releases")
            }

            pagedDataListCarousel(
                newReleases,
                "releases",
                viewModel::loadNewReleases
            ) { album ->
                album.clickableListItem {
                    show { newSpotifyAlbumFragment(album) }
                }
            }

            headerItem {
                id("tracks-header")
                text("Top tracks")
            }

            pagedDataListCarousel(
                topTracks,
                "tracks",
                viewModel::loadDailyViralTracks
            ) { topTrack ->
                TopTrackItemBindingModel_()
                    .id(topTrack.track.id)
                    .track(topTrack)
                    .itemClicked(View.OnClickListener {
                        show { newSpotifyTrackVideosFragment(topTrack.track) }
                    })
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
        inflater,
        R.layout.fragment_spotify_dashboard,
        container,
        false
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
