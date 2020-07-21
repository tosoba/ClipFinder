package com.example.spotify.album.ui

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.airbnb.mvrx.*
import com.example.coreandroid.*
import com.example.coreandroid.base.IFragmentFactory
import com.example.coreandroid.di.EpoxyHandlerQualifier
import com.example.coreandroid.model.LoadedSuccessfully
import com.example.coreandroid.model.Loading
import com.example.coreandroid.model.LoadingFailed
import com.example.coreandroid.model.spotify.Album
import com.example.coreandroid.model.spotify.Track
import com.example.coreandroid.model.spotify.clickableListItem
import com.example.coreandroid.util.carousel
import com.example.coreandroid.util.ext.*
import com.example.coreandroid.util.typedController
import com.example.coreandroid.util.withModelsFrom
import com.example.spotify.album.R
import com.example.spotify.album.databinding.FragmentSpotifyAlbumBinding
import com.wada811.lifecycledispose.disposeOnDestroy
import kotlinx.android.synthetic.main.fragment_spotify_album.*
import org.koin.android.ext.android.inject

class SpotifyAlbumFragment : BaseMvRxFragment(), NavigationCapable {

    override val factory: IFragmentFactory by inject()

    private val builder by inject<Handler>(EpoxyHandlerQualifier.BUILDER)
    private val differ by inject<Handler>(EpoxyHandlerQualifier.DIFFER)

    private val viewModel: SpotifyAlbumViewModel by fragmentViewModel()

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
                        viewModel.loadAlbumsArtists(state.album.artists.map { it.id })
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

            if (state.tracks.value.isEmpty()) when (state.tracks.status) {
                is Loading -> loadingIndicator {
                    id("loading-indicator-tracks")
                }

                is LoadingFailed<*> -> reloadControl {
                    id("tracks-reload-control")
                    onReloadClicked(View.OnClickListener {
                        viewModel.loadTracksFromAlbum(state.album.id)
                    })
                    message("Error occurred lmao") //TODO: error msg
                }
            } else carousel {
                id("tracks")
                withModelsFrom<Track>(
                    items = state.tracks.value,
                    extraModels = when (state.tracks.status) {
                        is LoadingFailed<*> -> listOf(
                            ReloadControlBindingModel_()
                                .id("reload-tracks")
                                .message("Error occurred")
                                .onReloadClicked(View.OnClickListener {
                                    viewModel.loadTracksFromAlbum(state.album.id)
                                })
                        )

                        else -> if (state.tracks.canLoadMore) listOf(
                            LoadingIndicatorBindingModel_()
                                .id("loading-more-tracks")
                                .onBind { _, _, _ ->
                                    viewModel.loadTracksFromAlbum(state.album.id)
                                }
                        ) else emptyList()
                    }
                ) { track ->
                    TrackPopularityItemBindingModel_()
                        .id(track.id)
                        .track(track) //TODO: navigation + a nicer layout
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = DataBindingUtil.inflate<FragmentSpotifyAlbumBinding>(
        inflater, R.layout.fragment_spotify_album, container, false
    ).apply {
        album = this@SpotifyAlbumFragment.album
        enableSpotifyPlayButton { loadAlbum(this@SpotifyAlbumFragment.album) }
        albumFavouriteFab.setOnClickListener { viewModel.toggleAlbumFavouriteState() }
        albumToolbarGradientBackgroundView
            .loadBackgroundGradient(this@SpotifyAlbumFragment.album.iconUrl)
            .disposeOnDestroy(this@SpotifyAlbumFragment)
        albumRecyclerView.setController(epoxyController)
        albumToolbar.setupWithBackNavigation(requireActivity() as? AppCompatActivity)
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.selectSubscribe(this, SpotifyAlbumViewState::isSavedAsFavourite) {
            album_favourite_fab?.setImageDrawable(
                ContextCompat.getDrawable(
                    view.context,
                    if (it.value) R.drawable.delete else R.drawable.favourite
                )
            )
            album_favourite_fab?.hideAndShow()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = false

    override fun invalidate() = withState(viewModel, epoxyController::setData)

    companion object {
        fun newInstance(album: Album) = SpotifyAlbumFragment().apply {
            arguments = Bundle().apply { putParcelable(MvRx.KEY_ARG, album) }
        }
    }
}
