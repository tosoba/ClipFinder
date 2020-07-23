package com.example.spotify.dashboard.ui

import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.annotation.StringRes
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
import com.example.spotify.dashboard.R
import com.example.spotify.dashboard.databinding.FragmentSpotifyDashboardBinding
import kotlinx.android.synthetic.main.fragment_spotify_dashboard.*
import org.koin.android.ext.android.inject

class SpotifyDashboardFragment : BaseMvRxFragment(), HasMainToolbar, NavigationCapable {

    override val factory: IFragmentFactory by inject()

    private val builder by inject<Handler>(EpoxyHandlerQualifier.BUILDER)
    private val differ by inject<Handler>(EpoxyHandlerQualifier.DIFFER)

    private val viewModel: SpotifyDashboardViewModel by fragmentViewModel()

    override val toolbar: Toolbar get() = dashboard_toolbar

    private val epoxyController by lazy(LazyThreadSafetyMode.NONE) {
        typedController(builder, differ, viewModel) { (categories, playlists, topTracks, newReleases) ->
            fun <Value> pagedDataListCarouselWithHeader(
                data: PagedDataList<Value>,
                @StringRes headerRes: Int,
                idSuffix: String,
                loadItems: () -> Unit,
                buildItem: (Value) -> EpoxyModel<*>
            ) {
                headerItem {
                    id("header-$idSuffix")
                    text(getString(headerRes))
                }

                val (value, status) = data
                if (value.isEmpty()) when (status) {
                    is Loading -> loadingIndicator {
                        id("loading-indicator-$idSuffix")
                    }

                    is LoadingFailed<*> -> reloadControl {
                        id("reload-control-$idSuffix")
                        onReloadClicked(View.OnClickListener { loadItems() })
                        message(getString(R.string.error_occurred))
                    }
                } else carousel {
                    id(idSuffix)
                    withModelsFrom<List<Value>>(
                        items = value.chunked(2),
                        extraModels = when (status) {
                            is LoadingFailed<*> -> listOf(
                                ReloadControlBindingModel_()
                                    .id("reload-control-$idSuffix")
                                    .message(getString(R.string.error_occurred))
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

            pagedDataListCarouselWithHeader(
                categories,
                R.string.categories,
                "categories",
                viewModel::loadCategories
            ) { category ->
                category.clickableListItem {
                    show { newSpotifyCategoryFragment(category) }
                }
            }

            pagedDataListCarouselWithHeader(
                playlists,
                R.string.featured_playlists,
                "playlists",
                viewModel::loadFeaturedPlaylists
            ) { playlist ->
                playlist.clickableListItem {
                    show { newSpotifyPlaylistFragment(playlist) }
                }
            }

            pagedDataListCarouselWithHeader(
                newReleases,
                R.string.new_releases,
                "releases",
                viewModel::loadNewReleases
            ) { album ->
                album.clickableListItem {
                    show { newSpotifyAlbumFragment(album) }
                }
            }

            pagedDataListCarouselWithHeader(
                topTracks,
                R.string.top_tracks,
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = DataBindingUtil.inflate<FragmentSpotifyDashboardBinding>(
        inflater, R.layout.fragment_spotify_dashboard, container, false
    ).apply {
        requireActivity().castAs<AppCompatActivity>()?.apply {
            setSupportActionBar(dashboardToolbar)
            showDrawerHamburger()
        }
        mainContentFragment?.disablePlayButton()
        dashboardRecyclerView.setController(epoxyController)
    }.root

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (toolbar.menu.size() == 0) {
            requireActivity().castAs<AppCompatActivity>()?.setSupportActionBar(toolbar)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = if (item.itemId == android.R.id.home
        && parentFragment?.childFragmentManager?.backStackEntryCount == 0) {
        activity?.castAs<NavigationDrawerController>()?.openDrawer()
        true
    } else false

    override fun invalidate() = withState(viewModel, epoxyController::setData)
}
