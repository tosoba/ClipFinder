package com.clipfinder.spotify.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.clipfinder.spotify.search.databinding.FragmentSpotifySearchBinding
import com.example.core.android.base.fragment.ItemListFragment
import com.example.core.android.model.DefaultLoadable
import com.example.core.android.spotify.model.*
import com.example.core.android.spotify.navigation.ISpotifyFragmentsFactory
import com.example.core.android.util.ext.newMvRxFragmentWith
import com.example.core.android.util.ext.parentFragmentViewModel
import com.example.core.android.util.ext.show
import com.example.core.android.view.epoxy.loadableCollectionController
import com.example.core.android.view.viewpager.adapter.TitledCustomCurrentStatePagerAdapter
import org.koin.android.ext.android.inject
import kotlin.reflect.KProperty1

class SpotifySearchFragment : BaseMvRxFragment() {
    private val viewModel: SpotifySearchViewModel by fragmentViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = FragmentSpotifySearchBinding.inflate(inflater, container, false)
        .apply {
            val adapter = TitledCustomCurrentStatePagerAdapter(
                childFragmentManager,
                arrayOf(
                    getString(R.string.albums) to itemListFragment<AlbumListFragment>(),
                    getString(R.string.artists) to itemListFragment<ArtistListFragment>(),
                    getString(R.string.playlists) to itemListFragment<PlaylistListFragment>(),
                    getString(R.string.tracks) to itemListFragment<TrackListFragment>()
                )
            )
            spotifyTabViewPager.offscreenPageLimit = adapter.count - 1
            spotifyTabViewPager.adapter = adapter
            spotifyTabLayout.setupWithViewPager(spotifyTabViewPager)
        }
        .root

    private inline fun <reified F : ItemListFragment<SpotifySearchState>> itemListFragment(): F {
        return newMvRxFragmentWith(ItemListFragment.Args(3, 4, 5))
    }

    override fun invalidate() = Unit

    companion object {
        fun newInstanceWithQuery(query: String): SpotifySearchFragment = newMvRxFragmentWith(query)

        abstract class BaseListFragment<I> : ItemListFragment<SpotifySearchState>() {
            protected val viewModel: SpotifySearchViewModel by parentFragmentViewModel()
            protected val factory: ISpotifyFragmentsFactory by inject()
            protected abstract val prop: KProperty1<SpotifySearchState, DefaultLoadable<Collection<I>>>
            protected abstract val search: () -> Unit
            protected abstract val clearError: () -> Unit

            override val epoxyController: TypedEpoxyController<SpotifySearchState> by lazy(LazyThreadSafetyMode.NONE) {
                loadableCollectionController(
                    prop,
                    loadMore = search,
                    reloadClicked = search,
                    clearFailure = clearError,
                    buildItem = ::buildItem
                )
            }

            protected abstract fun buildItem(item: I): EpoxyModel<*>
            override fun invalidate() = withState(viewModel, epoxyController::setData)
        }

        class AlbumListFragment : BaseListFragment<Album>() {
            override val prop = SpotifySearchState::albums
            override val search: () -> Unit get() = viewModel::searchAlbums
            override val clearError: () -> Unit get() = viewModel::clearAlbumsError
            override fun buildItem(item: Album): EpoxyModel<*> = item.clickableListItem {
                show { factory.newSpotifyAlbumFragment(item) }
            }
        }

        class ArtistListFragment : BaseListFragment<Artist>() {
            override val prop = SpotifySearchState::artists
            override val search: () -> Unit get() = viewModel::searchArtists
            override val clearError: () -> Unit get() = viewModel::clearArtistsError
            override fun buildItem(item: Artist): EpoxyModel<*> = item.clickableListItem {
                show { factory.newSpotifyArtistFragment(item) }
            }
        }

        class PlaylistListFragment : BaseListFragment<Playlist>() {
            override val prop = SpotifySearchState::playlists
            override val search: () -> Unit get() = viewModel::searchPlaylists
            override val clearError: () -> Unit get() = viewModel::clearPlaylistsError
            override fun buildItem(item: Playlist): EpoxyModel<*> = item.clickableListItem {
                show { factory.newSpotifyPlaylistFragment(item) }
            }
        }

        class TrackListFragment : BaseListFragment<Track>() {
            override val prop = SpotifySearchState::tracks
            override val search: () -> Unit get() = viewModel::searchTracks
            override val clearError: () -> Unit get() = viewModel::clearTracksError
            override fun buildItem(item: Track): EpoxyModel<*> = item.clickableListItem {
                show { factory.newSpotifyTrackVideosFragment(item) }
            }
        }
    }
}
