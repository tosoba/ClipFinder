package com.example.spotifyartist

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.airbnb.mvrx.*
import com.example.coreandroid.base.IFragmentFactory
import com.example.coreandroid.base.fragment.GoesToPreviousStateOnBackPressed
import com.example.coreandroid.headerItem
import com.example.coreandroid.lifecycle.ConnectivityComponent
import com.example.coreandroid.lifecycle.DisposablesComponent
import com.example.coreandroid.loadingIndicator
import com.example.coreandroid.model.LoadedSuccessfully
import com.example.coreandroid.model.Loading
import com.example.coreandroid.model.LoadingFailed
import com.example.coreandroid.model.spotify.Artist
import com.example.coreandroid.model.spotify.clickableListItem
import com.example.coreandroid.reloadControl
import com.example.coreandroid.util.asyncController
import com.example.coreandroid.util.carousel
import com.example.coreandroid.util.ext.*
import com.example.coreandroid.util.withModelsFrom
import com.example.spotifyartist.databinding.FragmentArtistBinding
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named

class ArtistFragment : BaseMvRxFragment(), NavigationCapable, GoesToPreviousStateOnBackPressed {

    override fun invalidate() = withState(viewModel) { state -> epoxyController.setData(state) }

    override val factory: IFragmentFactory by inject()

    private val builder by inject<Handler>(named("builder"))
    private val differ by inject<Handler>(named("differ"))

    private val argArtist: Artist by args()

    private val viewModel: ArtistViewModel by fragmentViewModel()

    private fun usingCurrentArtistsId(block: (String) -> Unit) {
        withState(viewModel) { state ->
            state.artists.value.lastOrNull()?.id?.let(block)
        }
    }

    private val epoxyController by lazy {
        asyncController(builder, differ, viewModel) { state ->
            headerItem {
                id("albums-header")
                text("Albums")
            }

            when (state.albums.status) {
                is Loading -> loadingIndicator {
                    id("loading-indicator-albums")
                }

                is LoadingFailed<*> -> reloadControl {
                    id("albums-reload-control")
                    onReloadClicked(View.OnClickListener {
                        usingCurrentArtistsId { viewModel.loadAlbumsFromArtist(it) }
                    })
                    message("Error occurred lmao") //TODO: error msg
                }

                is LoadedSuccessfully -> carousel {
                    id("albums")
                    withModelsFrom(state.albums.value) { album ->
                        album.clickableListItem {
                            show { newSpotifyAlbumFragment(album) }
                        }
                    }
                }
            }

            headerItem {
                id("related-artists-header")
                text("Related artists")
            }

            when (state.relatedArtists.status) {
                is Loading -> loadingIndicator {
                    id("loading-indicator-artists")
                }

                is LoadingFailed<*> -> reloadControl {
                    id("related-artists-reload-control")
                    onReloadClicked(View.OnClickListener {
                        usingCurrentArtistsId { viewModel.loadRelatedArtists(it) }
                    })
                    message("Error occurred lmao") //TODO: error msg
                }

                is LoadedSuccessfully -> carousel {
                    id("related-artists")
                    withModelsFrom(state.relatedArtists.value) { artist ->
                        artist.clickableListItem {
                            viewModel.updateArtist(artist)
                        }
                    }
                }
            }

            headerItem {
                id("top-tracks-header")
                text("Top tracks")
            }

            when (state.topTracks.status) {
                is Loading -> loadingIndicator {
                    id("loading-indicator-top-tracks")
                }

                is LoadingFailed<*> -> reloadControl {
                    id("top-tracks-reload-control")
                    onReloadClicked(View.OnClickListener {
                        usingCurrentArtistsId { viewModel.loadTopTracksFromArtist(it) }
                    })
                    message("Error occurred lmao") //TODO: error msg
                }

                is LoadedSuccessfully -> carousel {
                    id("top-tracks")
                    withModelsFrom(state.topTracks.value) { topTrack ->
                        topTrack.clickableListItem {
                            show { newSpotifyTrackVideosFragment(topTrack) }
                        }
                    }
                }
            }
        }
    }

    private val view: ArtistViewBinding by lazy {
        ArtistViewBinding(
                onFavouriteBtnClickListener = View.OnClickListener {
                    viewModel.toggleArtistFavouriteState()
                    //TODO: add a callback that will show a toast with a msg saying: added/deleted from favs
                },
                artist = MutableLiveData<Artist>().apply { value = argArtist }
        )
    }

    private val connectivityComponent: ConnectivityComponent by lazy {
        reloadingConnectivityComponent(viewModel::loadMissingData) {
            withState(viewModel) {
                it.albums.status is LoadingFailed<*> ||
                        it.relatedArtists.status is LoadingFailed<*> ||
                        it.topTracks.status is LoadingFailed<*>
            }

        }
    }

    private val disposablesComponent = DisposablesComponent()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentArtistBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_artist, container, false)

        viewModel.selectSubscribe(this, ArtistViewState::isSavedAsFavourite) {
            binding.artistFavouriteFab.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                    if (it.value) R.drawable.delete else R.drawable.favourite))
            binding.artistFavouriteFab.hideAndShow()
        }

        viewModel.selectSubscribe(this, ArtistViewState::artists) { artists ->
            artists.value.lastOrNull()?.let {
                view.artist.value = it
                binding.artistToolbarGradientBackgroundView.loadBackgroundGradient(it.iconUrl, disposablesComponent)
                binding.executePendingBindings()
            } ?: backPressedWithNoPreviousStateController?.onBackPressedWithNoPreviousState()
        }

        mainContentFragment?.disablePlayButton()

        return binding.apply {
            lifecycleOwner = this@ArtistFragment
            view = this@ArtistFragment.view
            artistToolbar.setupWithBackNavigation(appCompatActivity, ::onBackPressed)
            artistRecyclerView.apply {
                setController(epoxyController)
                //TODO: animation
            }
        }.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        lifecycle.addObserver(disposablesComponent)
        lifecycle.addObserver(connectivityComponent)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = false

    override fun onBackPressed() {
        viewModel.onBackPressed()
    }

    companion object {
        fun newInstance(artist: Artist) = ArtistFragment().apply {
            arguments = Bundle().apply { putParcelable(MvRx.KEY_ARG, artist) }
        }
    }
}
