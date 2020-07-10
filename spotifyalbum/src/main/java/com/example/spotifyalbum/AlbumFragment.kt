package com.example.spotifyalbum

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.mvrx.*
import com.example.coreandroid.*
import com.example.coreandroid.base.IFragmentFactory
import com.example.coreandroid.lifecycle.ConnectivityComponent
import com.example.coreandroid.lifecycle.DisposablesComponent
import com.example.coreandroid.model.LoadedSuccessfully
import com.example.coreandroid.model.Loading
import com.example.coreandroid.model.LoadingFailed
import com.example.coreandroid.model.spotify.Album
import com.example.coreandroid.model.spotify.clickableListItem
import com.example.coreandroid.util.carousel
import com.example.coreandroid.util.ext.*
import com.example.coreandroid.util.typedController
import com.example.coreandroid.util.withModelsFrom
import com.example.spotifyalbum.databinding.FragmentAlbumBinding
import kotlinx.android.synthetic.main.fragment_album.*
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named

class AlbumFragment : BaseMvRxFragment(), NavigationCapable {

    override val factory: IFragmentFactory by inject()

    private val builder by inject<Handler>(named("builder"))
    private val differ by inject<Handler>(named("differ"))

    private val viewModel: AlbumViewModel by fragmentViewModel()

    private val album: Album by args()

    private val epoxyController by lazy(LazyThreadSafetyMode.NONE) {
        typedController(builder, differ, viewModel) { state ->
            headerItem {
                id("artists-header")
                text("Artists")
            }

            when (state.artists.status) {
                is Loading -> loadingIndicator {
                    id("loading-indicator-artists")
                }

                is LoadingFailed<*> -> reloadControl {
                    id("artists-reload-control")
                    onReloadClicked(View.OnClickListener {
                        withState(viewModel) { state ->
                            viewModel.loadAlbumsArtists(state.album.artists.map { it.id })
                        }
                    })
                    message("Error occurred lmao") //TODO: error msg
                }

                is LoadedSuccessfully -> carousel {
                    id("artists")
                    withModelsFrom(state.artists.value) { artist ->
                        artist.clickableListItem {
                            show { newSpotifyArtistFragment(artist) }
                        }
                    }
                }
            }

            headerItem {
                id("tracks-header")
                text("Tracks")
            }

            val loadTracks: () -> Unit = {
                withState(viewModel) { state -> viewModel.loadTracksFromAlbum(state.album.id) }
            }

            fun tracksCarousel(extraModels: Collection<EpoxyModel<*>>) {
                carousel {
                    id("tracks")
                    withModelsFrom(
                        items = state.tracks.value,
                        extraModels = extraModels
                    ) { track ->
                        TrackPopularityItemBindingModel_()
                            .id(track.id)
                            .track(track) //TODO: navigation + a nicer layout
                    }
                }
            }

            if (state.tracks.value.isEmpty()) {
                when (state.tracks.status) {
                    is Loading -> loadingIndicator {
                        id("loading-indicator-tracks")
                    }

                    is LoadingFailed<*> -> reloadControl {
                        id("tracks-reload-control")
                        onReloadClicked(View.OnClickListener { loadTracks() })
                        message("Error occurred lmao") //TODO: error msg
                    }
                }
            } else {
                tracksCarousel(extraModels = when (state.tracks.status) {
                    is Loading -> listOf(LoadingIndicatorBindingModel_()
                        .id("loading-more-tracks"))
                    is LoadingFailed<*> -> listOf(ReloadControlBindingModel_()
                        .message("Error occurred")
                        .onReloadClicked(View.OnClickListener { loadTracks() }))
                    else -> emptyList()
                })
            }
        }
    }

    private val view: AlbumView by lazy {
        AlbumView(
            album = album,
            onFavouriteBtnClickListener = View.OnClickListener {
                viewModel.toggleAlbumFavouriteState()
            } //TODO: test this
        )
    }

    private val connectivityComponent: ConnectivityComponent by lazy {
        reloadingConnectivityComponent(::loadData) {
            withState(viewModel) {
                (it.tracks.loadingFailed && it.tracks.value.isEmpty()) || it.artists.loadingFailed
            }
        }
    }

    private val disposablesComponent = DisposablesComponent()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentAlbumBinding>(
            inflater,
            R.layout.fragment_album,
            container,
            false
        )
        enableSpotifyPlayButton { loadAlbum(album) }
        return binding.apply {
            view = this@AlbumFragment.view
            albumToolbarGradientBackgroundView.loadBackgroundGradient(album.iconUrl, disposablesComponent)
            albumRecyclerView.apply {
                setController(epoxyController)
                //TODO: animation
            }
            albumToolbar.setupWithBackNavigation(requireActivity() as? AppCompatActivity)
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.selectSubscribe(this, AlbumViewState::isSavedAsFavourite) {
            album_favourite_fab?.setImageDrawable(ContextCompat.getDrawable(view.context,
                if (it.value) R.drawable.delete else R.drawable.favourite))
            album_favourite_fab?.hideAndShow()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        lifecycle.addObserver(disposablesComponent)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(connectivityComponent)
    }

    override fun invalidate() = withState(viewModel) { state -> epoxyController.setData(state) }

    private fun loadData() = viewModel.loadAlbumData(album)

    companion object {
        fun newInstance(album: Album) = AlbumFragment().apply {
            arguments = Bundle().apply { putParcelable(MvRx.KEY_ARG, album) }
        }
    }
}
