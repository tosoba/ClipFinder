package com.example.spotifysearch.spotify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.*
import com.example.core.android.base.IFragmentFactory
import com.example.core.android.base.fragment.ISearchFragment
import com.example.core.android.base.fragment.ItemListFragment
import com.example.core.android.lifecycle.ConnectivityComponent
import com.example.core.android.model.HoldsData
import com.example.core.android.model.LoadingFailed
import com.example.core.android.model.spotify.*
import com.example.core.android.util.ext.NavigationCapable
import com.example.core.android.util.ext.parentFragmentViewModel
import com.example.core.android.util.ext.reloadingConnectivityComponent
import com.example.core.android.util.ext.show
import com.example.core.android.view.OnPageChangeListener
import com.example.core.android.view.OnTabSelectedListener
import com.example.core.android.view.epoxy.injectedItemListController
import com.example.core.android.view.viewpager.adapter.CustomCurrentStatePagerAdapter
import com.example.spotifysearch.R
import com.example.spotifysearch.databinding.FragmentSpotifySearchBinding
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_spotify_search.*
import org.koin.android.ext.android.inject
import kotlin.reflect.KProperty1

class SpotifySearchFragment : BaseMvRxFragment(), ISearchFragment, NavigationCapable {

    override fun invalidate() = Unit

    override val factory: IFragmentFactory by inject()

    private val argQuery: String by args()

    private val viewModel: SpotifySearchViewModel by fragmentViewModel()

    //TODO: replace this with a function that calls onNewQuery to get rid of useless stateful property
    override var query: String = ""
        set(value) {
            if (field == value) return
            field = value
            viewModel.onNewQuery(value)
        }

    private val onSpotifyTabSelectedListener = object : OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.let { spotify_tab_view_pager?.currentItem = it.position }
        }
    }

    private val onSpotifyPageChangedListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            spotify_tab_layout?.getTabAt(position)?.select()
        }
    }

    private val itemListFragmentArgs: ItemListFragment.Args by lazy {
        ItemListFragment.Args(3, 4, 5)
    }

    //TODO: in the future do all searches separately
    // to avoid loading more of all types of items when one of the lists is scrolled all the way down

    private val pagerAdapter: CustomCurrentStatePagerAdapter by lazy {
        CustomCurrentStatePagerAdapter(
            childFragmentManager,
            arrayOf(
                ItemListFragment.new<AlbumListFragment, SpotifySearchViewState>(itemListFragmentArgs),
                ItemListFragment.new<ArtistListFragment, SpotifySearchViewState>(itemListFragmentArgs),
                ItemListFragment.new<PlaylistListFragment, SpotifySearchViewState>(itemListFragmentArgs),
                ItemListFragment.new<TrackListFragment, SpotifySearchViewState>(itemListFragmentArgs)
            )
        )
    }

    private val viewBinding: SpotifySearchViewBinding by lazy {
        SpotifySearchViewBinding(
            pagerAdapter = pagerAdapter,
            onTabSelectedListener = onSpotifyTabSelectedListener,
            onPageChangeListener = onSpotifyPageChangedListener
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = DataBindingUtil.inflate<FragmentSpotifySearchBinding>(
        inflater, R.layout.fragment_spotify_search, container, false
    ).apply {
        spotifySearchView = viewBinding
        spotifyTabViewPager.offscreenPageLimit = 3
    }.root

    private val connectivityComponent: ConnectivityComponent by lazy {
        reloadingConnectivityComponent(viewModel::searchWithLastQuery) {
            withState(viewModel) { it.status is LoadingFailed<*> }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(connectivityComponent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) query = argQuery
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
            override val prop: KProperty1<SpotifySearchViewState, HoldsData<Collection<Album>>> = SpotifySearchViewState::albums
            override fun buildItem(
                item: Album
            ): EpoxyModel<*> = item.clickableListItem {
                show { factory.newSpotifyAlbumFragment(item) }
            }
        }

        class ArtistListFragment : BaseListFragment<Artist>() {
            override val prop: KProperty1<SpotifySearchViewState, HoldsData<Collection<Artist>>> = SpotifySearchViewState::artists
            override fun buildItem(
                item: Artist
            ): EpoxyModel<*> = item.clickableListItem {
                show { factory.newSpotifyArtistFragment(item) }
            }
        }

        class PlaylistListFragment : BaseListFragment<Playlist>() {
            override val prop: KProperty1<SpotifySearchViewState, HoldsData<Collection<Playlist>>> = SpotifySearchViewState::playlists
            override fun buildItem(
                item: Playlist
            ): EpoxyModel<*> = item.clickableListItem {
                show { factory.newSpotifyPlaylistFragment(item) }
            }
        }

        class TrackListFragment : BaseListFragment<Track>() {
            override val prop: KProperty1<SpotifySearchViewState, HoldsData<Collection<Track>>> = SpotifySearchViewState::tracks
            override fun buildItem(
                item: Track
            ): EpoxyModel<*> = item.clickableListItem {
                show { factory.newSpotifyTrackVideosFragment(item) }
            }
        }
    }
}
