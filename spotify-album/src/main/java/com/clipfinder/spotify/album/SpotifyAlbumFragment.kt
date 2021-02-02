package com.clipfinder.spotify.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.args
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.clipfinder.spotify.album.databinding.FragmentSpotifyAlbumBinding
import com.clipfinder.core.android.spotify.TrackPopularityItemBindingModel_
import com.clipfinder.core.android.spotify.ext.enableSpotifyPlayButton
import com.clipfinder.core.android.spotify.model.Album
import com.clipfinder.core.android.spotify.model.clickableListItem
import com.clipfinder.core.android.spotify.navigation.ISpotifyFragmentsFactory
import com.clipfinder.core.android.util.ext.loadBackgroundGradient
import com.clipfinder.core.android.util.ext.newMvRxFragmentWith
import com.clipfinder.core.android.util.ext.setupWithBackNavigation
import com.clipfinder.core.android.util.ext.show
import com.clipfinder.core.android.view.epoxy.injectedTypedController
import com.clipfinder.core.android.view.epoxy.loadableCarouselWithHeader
import com.wada811.lifecycledispose.disposeOnDestroy
import org.koin.android.ext.android.inject

class SpotifyAlbumFragment : BaseMvRxFragment() {
    private val factory: ISpotifyFragmentsFactory by inject()
    private val viewModel: SpotifyAlbumViewModel by fragmentViewModel()
    private val album: Album by args()

    private val epoxyController: TypedEpoxyController<SpotifyAlbumViewState> by lazy(LazyThreadSafetyMode.NONE) {
        injectedTypedController<SpotifyAlbumViewState> { (_, artists, tracks) ->
            loadableCarouselWithHeader(
                requireContext(),
                artists,
                R.string.artists,
                "artists",
                viewModel::loadAlbumsArtists,
                viewModel::clearArtistsError
            ) { artist ->
                artist.clickableListItem {
                    show { factory.newSpotifyArtistFragment(artist) }
                }
            }

            loadableCarouselWithHeader(
                requireContext(),
                tracks,
                R.string.tracks,
                "tracks",
                viewModel::loadTracksFromAlbum,
                viewModel::clearTracksError
            ) { track ->
                TrackPopularityItemBindingModel_()
                    .id(track.id)
                    .track(track) //TODO: navigation + a nicer layout
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = FragmentSpotifyAlbumBinding.inflate(inflater, container, false)
        .apply {
            album = this@SpotifyAlbumFragment.album
            enableSpotifyPlayButton { loadAlbum(this@SpotifyAlbumFragment.album) }
            albumFavouriteFab.setOnClickListener { }
            albumToolbarGradientBackgroundView
                .loadBackgroundGradient(this@SpotifyAlbumFragment.album.iconUrl)
                .disposeOnDestroy(this@SpotifyAlbumFragment)
            albumRecyclerView.setController(epoxyController)
            albumToolbar.setupWithBackNavigation(requireActivity() as? AppCompatActivity)
        }
        .root

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = false

    override fun invalidate() = withState(viewModel, epoxyController::setData)

    companion object {
        fun new(album: Album): SpotifyAlbumFragment = newMvRxFragmentWith(album)
    }
}
