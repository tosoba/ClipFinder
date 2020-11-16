package com.clipfinder.spotify.dashboard

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyVisibilityTracker
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.clipfinder.spotify.dashboard.databinding.FragmentSpotifyDashboardBinding
import com.example.core.android.base.fragment.HasMainToolbar
import com.example.core.android.base.handler.NavigationDrawerController
import com.example.core.android.spotify.TopTrackItemBindingModel_
import com.example.core.android.spotify.model.clickableListItem
import com.example.core.android.spotify.navigation.ISpotifyFragmentsFactory
import com.example.core.android.util.ext.mainContentFragment
import com.example.core.android.util.ext.show
import com.example.core.android.util.ext.showDrawerHamburger
import com.example.core.android.view.epoxy.Column
import com.example.core.android.view.epoxy.injectedTypedController
import com.example.core.android.view.epoxy.loadableCarouselWithHeader
import com.example.core.ext.castAs
import org.koin.android.ext.android.inject

class SpotifyDashboardFragment : BaseMvRxFragment(), HasMainToolbar {
    private val navDestinations: ISpotifyFragmentsFactory by inject()
    private val viewModel: SpotifyDashboardViewModel by fragmentViewModel()

    private lateinit var binding: FragmentSpotifyDashboardBinding
    override val toolbar: Toolbar
        get() = binding.dashboardToolbar

    private val epoxyController: TypedEpoxyController<SpotifyDashboardState> by lazy(LazyThreadSafetyMode.NONE) {
        injectedTypedController<SpotifyDashboardState> { (categories, playlists, topTracks, newReleases) ->
            fun <I> Collection<I>.column(buildItem: (I) -> EpoxyModel<*>): Column = Column(map(buildItem))

            loadableCarouselWithHeader(
                requireContext(),
                categories,
                R.string.categories,
                "categories",
                viewModel::loadCategories,
                viewModel::clearCategoriesError,
                { it.chunked(2) }
            ) { chunk ->
                chunk.column { category ->
                    category.clickableListItem {
                        show { navDestinations.newSpotifyCategoryFragment(category) }
                    }
                }
            }

            loadableCarouselWithHeader(
                requireContext(),
                playlists,
                R.string.featured_playlists,
                "playlists",
                viewModel::loadFeaturedPlaylists,
                viewModel::clearFeaturedPlaylistsError,
                { it.chunked(2) }
            ) { chunk ->
                chunk.column { playlist ->
                    playlist.clickableListItem {
                        show { navDestinations.newSpotifyPlaylistFragment(playlist) }
                    }
                }
            }

            loadableCarouselWithHeader(
                requireContext(),
                newReleases,
                R.string.new_releases,
                "releases",
                viewModel::loadNewReleases,
                viewModel::clearNewReleasesError,
                { it.chunked(2) }
            ) { chunk ->
                chunk.column { album ->
                    album.clickableListItem {
                        show { navDestinations.newSpotifyAlbumFragment(album) }
                    }
                }
            }

            loadableCarouselWithHeader(
                requireContext(),
                topTracks,
                R.string.top_tracks,
                "top-tracks",
                viewModel::loadViralTracks,
                viewModel::clearViralTracksError,
                { it.chunked(2) }
            ) { chunk ->
                chunk.column { topTrack ->
                    TopTrackItemBindingModel_()
                        .id(topTrack.track.id)
                        .track(topTrack)
                        .itemClicked { _ ->
                            show { navDestinations.newSpotifyTrackVideosFragment(topTrack.track) }
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = FragmentSpotifyDashboardBinding.inflate(inflater, container, false)
        .apply {
            binding = this
            requireActivity().castAs<AppCompatActivity>()?.apply {
                setSupportActionBar(dashboardToolbar)
                showDrawerHamburger()
            }
            mainContentFragment?.disablePlayButton()
            dashboardRecyclerView.setController(epoxyController)
            EpoxyVisibilityTracker().attach(dashboardRecyclerView)
        }
        .root

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (toolbar.menu.size() == 0) {
            requireActivity().castAs<AppCompatActivity>()?.setSupportActionBar(toolbar)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = if (
        item.itemId == android.R.id.home && parentFragment?.childFragmentManager?.backStackEntryCount == 0
    ) {
        activity?.castAs<NavigationDrawerController>()?.openDrawer()
        true
    } else false

    override fun invalidate() = withState(viewModel, epoxyController::setData)
}
