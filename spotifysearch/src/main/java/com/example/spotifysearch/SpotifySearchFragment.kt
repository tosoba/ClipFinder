package com.example.spotifysearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.*
import com.example.core.android.base.IFragmentFactory
import com.example.core.android.base.fragment.ISearchFragment
import com.example.core.android.base.fragment.ItemListFragment
import com.example.core.android.model.HoldsData
import com.example.core.android.model.spotify.*
import com.example.core.android.util.ext.NavigationCapable
import com.example.core.android.util.ext.parentFragmentViewModel
import com.example.core.android.util.ext.show
import com.example.core.android.view.epoxy.injectedItemListController
import com.example.core.android.view.viewpager.adapter.TitledCustomCurrentStatePagerAdapter
import kotlinx.android.synthetic.main.fragment_spotify_search.view.*
import org.koin.android.ext.android.inject
import kotlin.reflect.KProperty1

class SpotifySearchFragment : BaseMvRxFragment(), ISearchFragment {

    override fun invalidate() = Unit

    private val argQuery: String by args()

    private val viewModel: SpotifySearchViewModel by fragmentViewModel()

    //TODO: replace this with a function that calls onNewQuery to get rid of useless stateful property
    override var query: String = ""
        set(value) {
            if (field == value) return
            field = value
            viewModel.onNewQuery(value)
        }

    //TODO: in the future do all searches separately
    // to avoid loading more of all types of items when one of the lists is scrolled all the way down

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) query = argQuery
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.fragment_spotify_search, container, false
    ).also {
        it.spotify_tab_view_pager.offscreenPageLimit = 3
        it.spotify_tab_view_pager.adapter = TitledCustomCurrentStatePagerAdapter(
            childFragmentManager,
            arrayOf(
                getString(R.string.albums) to itemListFragment<AlbumListFragment>(),
                getString(R.string.artists) to itemListFragment<ArtistListFragment>(),
                getString(R.string.playlists) to itemListFragment<PlaylistListFragment>(),
                getString(R.string.tracks) to itemListFragment<TrackListFragment>()
            )
        )
        it.spotify_tab_layout.setupWithViewPager(it.spotify_tab_view_pager)
    }

    private inline fun <reified F : ItemListFragment<SpotifySearchViewState>> itemListFragment(): F {
        return ItemListFragment.new(ItemListFragment.Args(3, 4, 5))
    }

    companion object {
        fun newInstanceWithQuery(query: String) = SpotifySearchFragment().apply {
            arguments = Bundle().apply { putString(MvRx.KEY_ARG, query) }
        }

        abstract class BaseListFragment<I> : ItemListFragment<SpotifySearchViewState>(), NavigationCapable {
            private val viewModel: SpotifySearchViewModel by parentFragmentViewModel()
            override val factory: IFragmentFactory by inject()

            protected abstract val prop: KProperty1<SpotifySearchViewState, HoldsData<Collection<I>>>

            override val epoxyController: TypedEpoxyController<SpotifySearchViewState> by lazy(LazyThreadSafetyMode.NONE) {
                injectedItemListController(
                    prop,
                    loadMore = viewModel::searchWithLastQuery,
                    reloadClicked = viewModel::searchWithLastQuery,
                    buildItem = ::buildItem
                )
            }

            override fun invalidate() = withState(viewModel, epoxyController::setData)

            protected abstract fun buildItem(item: I): EpoxyModel<*>
        }

        class AlbumListFragment : BaseListFragment<Album>() {
            override val prop = SpotifySearchViewState::albums
            override fun buildItem(item: Album): EpoxyModel<*> = item.clickableListItem {
                show { factory.newSpotifyAlbumFragment(item) }
            }
        }

        class ArtistListFragment : BaseListFragment<Artist>() {
            override val prop = SpotifySearchViewState::artists
            override fun buildItem(item: Artist): EpoxyModel<*> = item.clickableListItem {
                show { factory.newSpotifyArtistFragment(item) }
            }
        }

        class PlaylistListFragment : BaseListFragment<Playlist>() {
            override val prop = SpotifySearchViewState::playlists
            override fun buildItem(item: Playlist): EpoxyModel<*> = item.clickableListItem {
                show { factory.newSpotifyPlaylistFragment(item) }
            }
        }

        class TrackListFragment : BaseListFragment<Track>() {
            override val prop = SpotifySearchViewState::tracks
            override fun buildItem(item: Track): EpoxyModel<*> = item.clickableListItem {
                show { factory.newSpotifyTrackVideosFragment(item) }
            }
        }
    }
}
