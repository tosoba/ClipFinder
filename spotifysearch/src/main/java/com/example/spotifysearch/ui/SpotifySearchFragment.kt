package com.example.spotifysearch.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.*
import com.example.core.android.base.IFragmentFactory
import com.example.core.android.base.fragment.ItemListFragment
import com.example.core.android.model.HoldsData
import com.example.core.android.model.spotify.*
import com.example.core.android.util.ext.NavigationCapable
import com.example.core.android.util.ext.parentFragmentViewModel
import com.example.core.android.util.ext.show
import com.example.core.android.view.epoxy.injectedItemListController
import com.example.core.android.view.viewpager.adapter.TitledCustomCurrentStatePagerAdapter
import com.example.spotifysearch.R
import com.example.spotifysearch.databinding.FragmentSpotifySearchBinding
import org.koin.android.ext.android.inject
import kotlin.reflect.KProperty1

class SpotifySearchFragment : BaseMvRxFragment() {

    private val viewModel: SpotifySearchViewModel by fragmentViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = FragmentSpotifySearchBinding.inflate(inflater, container, false).apply {
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
    }.root

    private inline fun <reified F : ItemListFragment<SpotifySearchViewState>> itemListFragment(): F {
        return ItemListFragment.new(ItemListFragment.Args(3, 4, 5))
    }

    override fun invalidate() = Unit

    companion object {
        fun newInstanceWithQuery(query: String) = SpotifySearchFragment().apply {
            arguments = Bundle().apply { putString(MvRx.KEY_ARG, query) }
        }

        abstract class BaseListFragment<I> :
            ItemListFragment<SpotifySearchViewState>(),
            NavigationCapable {

            protected val viewModel: SpotifySearchViewModel by parentFragmentViewModel()
            override val factory: IFragmentFactory by inject()

            protected abstract val prop: KProperty1<SpotifySearchViewState, HoldsData<Collection<I>>>

            override val epoxyController: TypedEpoxyController<SpotifySearchViewState> by lazy(
                LazyThreadSafetyMode.NONE
            ) {
                injectedItemListController(
                    prop,
                    loadMore = search,
                    reloadClicked = search,
                    buildItem = ::buildItem
                )
            }

            override fun invalidate() = withState(viewModel, epoxyController::setData)

            protected abstract fun buildItem(item: I): EpoxyModel<*>
            protected abstract val search: () -> Unit
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
