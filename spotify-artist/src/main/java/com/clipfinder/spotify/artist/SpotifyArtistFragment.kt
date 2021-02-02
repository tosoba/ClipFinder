package com.clipfinder.spotify.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.args
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.clipfinder.spotify.artist.databinding.FragmentSpotifyArtistBinding
import com.clipfinder.core.android.base.fragment.BackPressedHandler
import com.clipfinder.core.model.WithValue
import com.clipfinder.core.android.spotify.model.Artist
import com.clipfinder.core.android.spotify.model.clickableListItem
import com.clipfinder.core.android.spotify.navigation.ISpotifyFragmentsFactory
import com.clipfinder.core.android.util.ext.*
import com.clipfinder.core.android.view.epoxy.injectedTypedController
import com.clipfinder.core.android.view.epoxy.loadableCarouselWithHeader
import com.wada811.lifecycledispose.disposeOnDestroy
import org.koin.android.ext.android.inject

class SpotifyArtistFragment : BaseMvRxFragment(), BackPressedHandler {
    private val argArtist: Artist by args()
    private val viewModel: SpotifyArtistViewModel by fragmentViewModel()
    private val factory: ISpotifyFragmentsFactory by inject()

    private val epoxyController: TypedEpoxyController<SpotifyArtistViewState> by lazy(LazyThreadSafetyMode.NONE) {
        injectedTypedController<SpotifyArtistViewState> { (_, albums, topTracks, relatedArtists) ->
            loadableCarouselWithHeader(
                requireContext(),
                albums,
                R.string.albums,
                "albums",
                { viewModel.loadAlbumsFromArtist() },
                viewModel::clearAlbumsError
            ) { album ->
                album.clickableListItem {
                    show { factory.newSpotifyAlbumFragment(album) }
                }
            }

            loadableCarouselWithHeader(
                requireContext(),
                relatedArtists,
                R.string.related_artists,
                "related-artists",
                viewModel::loadRelatedArtists,
                viewModel::clearTopTracksError
            ) { artist ->
                artist.clickableListItem { viewModel.updateArtist(artist) }
            }

            loadableCarouselWithHeader(
                requireContext(),
                topTracks,
                R.string.top_tracks,
                "top-tracks",
                viewModel::loadTopTracksFromArtist,
                viewModel::clearRelatedArtistsError
            ) { topTrack ->
                topTrack.clickableListItem {
                    show { factory.newSpotifyTrackVideosFragment(topTrack) }
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentSpotifyArtistBinding.inflate(inflater, container, false)

        viewModel.selectSubscribe(this, SpotifyArtistViewState::isSavedAsFavourite) {
            if (it !is WithValue) return@selectSubscribe
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
            artists.lastOrNull()?.let {
                artist.value = it
                binding.artistToolbarGradientBackgroundView
                    .loadBackgroundGradient(it.iconUrl)
                    .disposeOnDestroy(this)
                binding.executePendingBindings()
            } ?: backPressedController?.onBackPressedWithNoPreviousState()
        }

        mainContentFragment?.disablePlayButton()

        return binding.apply {
            lifecycleOwner = this@SpotifyArtistFragment
            this.artist = artist
            artistFavouriteFab.setOnClickListener { }
            artistToolbar.setupWithBackNavigation(requireActivity() as AppCompatActivity, ::onBackPressed)
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
        fun new(artist: Artist): SpotifyArtistFragment = newMvRxFragmentWith(artist)
    }
}
