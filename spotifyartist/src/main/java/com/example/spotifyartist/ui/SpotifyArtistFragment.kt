package com.example.spotifyartist.ui

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.airbnb.mvrx.*
import com.example.coreandroid.base.IFragmentFactory
import com.example.coreandroid.base.fragment.GoesToPreviousStateOnBackPressed
import com.example.coreandroid.di.EpoxyHandlerQualifier
import com.example.coreandroid.headerItem
import com.example.coreandroid.loadingIndicator
import com.example.coreandroid.model.LoadedSuccessfully
import com.example.coreandroid.model.Loading
import com.example.coreandroid.model.LoadingFailed
import com.example.coreandroid.model.spotify.Artist
import com.example.coreandroid.model.spotify.clickableListItem
import com.example.coreandroid.reloadControl
import com.example.coreandroid.util.carousel
import com.example.coreandroid.util.ext.*
import com.example.coreandroid.util.typedController
import com.example.coreandroid.util.withModelsFrom
import com.example.spotifyartist.R
import com.example.spotifyartist.databinding.FragmentArtistBinding
import com.wada811.lifecycledispose.disposeOnDestroy
import org.koin.android.ext.android.inject

class SpotifyArtistFragment : BaseMvRxFragment(), NavigationCapable, GoesToPreviousStateOnBackPressed {

    override val factory: IFragmentFactory by inject()

    private val builder by inject<Handler>(EpoxyHandlerQualifier.BUILDER)
    private val differ by inject<Handler>(EpoxyHandlerQualifier.DIFFER)

    private val argArtist: Artist by args()

    private val viewModel: SpotifyArtistViewModel by fragmentViewModel()

    private val epoxyController by lazy(LazyThreadSafetyMode.NONE) {
        fun withCurrentArtistId(block: (String) -> Unit) = withState(viewModel) { (artists) ->
            artists.value.lastOrNull()?.id?.let(block)
        }

        typedController(builder, differ, viewModel) { (_, albums, topTracks, relatedArtists) ->
            headerItem {
                id("albums-header")
                text("Albums")
            }

            when (albums.status) {
                is Loading -> loadingIndicator {
                    id("loading-indicator-albums")
                }

                is LoadingFailed<*> -> reloadControl {
                    id("albums-reload-control")
                    onReloadClicked(View.OnClickListener {
                        withCurrentArtistId { viewModel.loadAlbumsFromArtist(it) }
                    })
                    message("Error occurred lmao") //TODO: error msg
                }

                is LoadedSuccessfully -> carousel {
                    id("albums")
                    withModelsFrom(albums.value) { album ->
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

            when (relatedArtists.status) {
                is Loading -> loadingIndicator {
                    id("loading-indicator-artists")
                }

                is LoadingFailed<*> -> reloadControl {
                    id("related-artists-reload-control")
                    onReloadClicked(View.OnClickListener {
                        withCurrentArtistId { viewModel.loadRelatedArtists(it) }
                    })
                    message("Error occurred lmao") //TODO: error msg
                }

                is LoadedSuccessfully -> carousel {
                    id("related-artists")
                    withModelsFrom(relatedArtists.value) { artist ->
                        artist.clickableListItem { viewModel.updateArtist(artist) }
                    }
                }
            }

            headerItem {
                id("top-tracks-header")
                text("Top tracks")
            }

            when (topTracks.status) {
                is Loading -> loadingIndicator {
                    id("loading-indicator-top-tracks")
                }

                is LoadingFailed<*> -> reloadControl {
                    id("top-tracks-reload-control")
                    onReloadClicked(View.OnClickListener {
                        withCurrentArtistId { viewModel.loadTopTracksFromArtist(it) }
                    })
                    message("Error occurred lmao") //TODO: error msg
                }

                is LoadedSuccessfully -> carousel {
                    id("top-tracks")
                    withModelsFrom(topTracks.value) { topTrack ->
                        topTrack.clickableListItem {
                            show { newSpotifyTrackVideosFragment(topTrack) }
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentArtistBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_artist, container, false
        )

        viewModel.selectSubscribe(this, SpotifyArtistViewState::isSavedAsFavourite) {
            binding.artistFavouriteFab.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    if (it.value) R.drawable.delete else R.drawable.favourite
                )
            )
            binding.artistFavouriteFab.hideAndShow()
        }

        val artist = MutableLiveData<Artist>().apply { value = argArtist }
        viewModel.selectSubscribe(this, SpotifyArtistViewState::artists) { artists ->
            artists.value.lastOrNull()?.let {
                artist.value = it
                binding.artistToolbarGradientBackgroundView
                    .loadBackgroundGradient(it.iconUrl)
                    .disposeOnDestroy(this)
                binding.executePendingBindings()
            } ?: backPressedWithNoPreviousStateController?.onBackPressedWithNoPreviousState()
        }

        mainContentFragment?.disablePlayButton()

        return binding.apply {
            lifecycleOwner = this@SpotifyArtistFragment
            this.artist = artist
            artistFavouriteFab.setOnClickListener { viewModel.toggleArtistFavouriteState() }
            artistToolbar.setupWithBackNavigation(
                requireActivity() as AppCompatActivity,
                ::onBackPressed
            )
            artistRecyclerView.setController(epoxyController)
        }.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onBackPressed() = viewModel.onBackPressed()

    override fun onOptionsItemSelected(item: MenuItem): Boolean = false

    override fun invalidate() = withState(viewModel, epoxyController::setData)

    companion object {
        fun newInstance(artist: Artist) = SpotifyArtistFragment().apply {
            arguments = Bundle().apply { putParcelable(MvRx.KEY_ARG, artist) }
        }
    }
}
