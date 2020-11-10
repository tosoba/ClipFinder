package com.example.spotify.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.example.core.android.base.fragment.ItemListFragment
import com.example.core.android.model.HoldsData
import com.example.core.android.spotify.model.*
import com.example.core.android.spotify.navigation.ISpotifyFragmentsFactory
import com.example.core.android.util.ext.newMvRxFragmentWith
import com.example.core.android.util.ext.parentFragmentViewModel
import com.example.core.android.util.ext.show
import com.example.core.android.view.epoxy.itemListController
import com.example.core.android.view.viewpager.adapter.TitledCustomCurrentStatePagerAdapter
import com.example.spotify.search.R
import com.example.spotify.search.databinding.FragmentSpotifySearchBinding
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

    private inline fun <reified F : ItemListFragment<SpotifySearchViewState>> itemListFragment(): F {
        return newMvRxFragmentWith(ItemListFragment.Args(3, 4, 5))
    }

    override fun invalidate() = Unit

    companion object {
        fun newInstanceWithQuery(query: String) = SpotifySearchFragment().apply {
            arguments = Bundle().apply { putString(MvRx.KEY_ARG, query) }
        }

        abstract class BaseListFragment<I> : ItemListFragment<SpotifySearchViewState>() {
            protected val viewModel: SpotifySearchViewModel by parentFragmentViewModel()
            protected val factory: ISpotifyFragmentsFactory by inject()
            protected abstract val prop: KProperty1<SpotifySearchViewState, HoldsData<Collection<I>>>

            override val epoxyController: TypedEpoxyController<SpotifySearchViewState> by lazy(
                LazyThreadSafetyMode.NONE
            ) {
                itemListController(
                    prop,
                    loadMore = search,
                    reloadClicked = search,
                    buildItem = ::buildItem
                )
            }

            protected abstract fun buildItem(item: I): EpoxyModel<*>
            protected abstract val search: () -> Unit
            override fun invalidate() = withState(viewModel, epoxyController::setData)
        }

        class AlbumListFragment : BaseListFragment<Album>() {
            override val prop = SpotifySearchViewState::albums
            override val search: () -> Unit get() = viewModel::searchAlbums
            override fun buildItem(item: Album): EpoxyModel<*> = item.clickableListItem {
                show { factory.newSpotifyAlbumFragment(item) }
            }
        }

        class ArtistListFragment : BaseListFragment<Artist>() {
            override val prop = SpotifySearchViewState::artists
            override val search: () -> Unit get() = viewModel::searchArtists
            override fun buildItem(item: Artist): EpoxyModel<*> = item.clickableListItem {
                show { factory.newSpotifyArtistFragment(item) }
            }
        }

        class PlaylistListFragment : BaseListFragment<Playlist>() {
            override val prop = SpotifySearchViewState::playlists
            override val search: () -> Unit get() = viewModel::searchPlaylists
            override fun buildItem(item: Playlist): EpoxyModel<*> = item.clickableListItem {
                show { factory.newSpotifyPlaylistFragment(item) }
            }
        }

        class TrackListFragment : BaseListFragment<Track>() {
            override val prop = SpotifySearchViewState::tracks
            override val search: () -> Unit get() = viewModel::searchTracks
            override fun buildItem(item: Track): EpoxyModel<*> = item.clickableListItem {
                show { factory.newSpotifyTrackVideosFragment(item) }
            }
        }
    }
}
