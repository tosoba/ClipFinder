package com.example.spotify.artist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.airbnb.mvrx.*
import com.example.core.android.base.IFragmentFactory
import com.example.core.android.base.fragment.GoesToPreviousStateOnBackPressed
import com.example.core.android.model.spotify.Artist
import com.example.core.android.model.spotify.clickableListItem
import com.example.core.android.util.ext.*
import com.example.core.android.view.epoxy.dataListCarouselWithHeader
import com.example.core.android.view.epoxy.injectedTypedController
import com.example.core.android.view.epoxy.pagedDataListCarouselWithHeader
import com.example.spotify.artist.R
import com.example.spotify.artist.databinding.FragmentSpotifyArtistBinding
import com.wada811.lifecycledispose.disposeOnDestroy
import org.koin.android.ext.android.inject

class SpotifyArtistFragment : BaseMvRxFragment(), NavigationCapable, GoesToPreviousStateOnBackPressed {

    override val factory: IFragmentFactory by inject()

    private val argArtist: Artist by args()

    private val viewModel: SpotifyArtistViewModel by fragmentViewModel()

    private val epoxyController by lazy(LazyThreadSafetyMode.NONE) {
        fun withCurrentArtistId(block: (String) -> Unit) = withState(viewModel) { (artists) ->
            artists.value.lastOrNull()?.id?.let(block)
        }

        injectedTypedController<SpotifyArtistViewState> { (_, albums, topTracks, relatedArtists) ->
            pagedDataListCarouselWithHeader(
                requireContext(),
                albums,
                R.string.albums,
                "albums",
                { withCurrentArtistId { viewModel.loadAlbumsFromArtist(it) } }
            ) { album ->
                album.clickableListItem {
                    show { newSpotifyAlbumFragment(album) }
                }
            }

            dataListCarouselWithHeader(
                requireContext(),
                relatedArtists,
                R.string.related_artists,
                "related-artists",
                { withCurrentArtistId { viewModel.loadRelatedArtists(it) } }
            ) { artist ->
                artist.clickableListItem { viewModel.updateArtist(artist) }
            }

            dataListCarouselWithHeader(
                requireContext(),
                topTracks,
                R.string.top_tracks,
                "top-tracks",
                { withCurrentArtistId { viewModel.loadTopTracksFromArtist(it) } }
            ) { topTrack ->
                topTrack.clickableListItem {
                    show { newSpotifyTrackVideosFragment(topTrack) }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSpotifyArtistBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_spotify_artist, container, false
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

        val artist = MutableLiveData<Artist>(argArtist)
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
