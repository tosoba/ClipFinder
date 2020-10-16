package com.example.spotify.album.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.args
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.example.core.android.spotify.TrackPopularityItemBindingModel_
import com.example.core.android.spotify.ext.enableSpotifyPlayButton
import com.example.core.android.spotify.model.Album
import com.example.core.android.spotify.model.clickableListItem
import com.example.core.android.spotify.navigation.ISpotifyFragmentsFactory
import com.example.core.android.util.ext.loadBackgroundGradient
import com.example.core.android.util.ext.newFragmentWithMvRxArg
import com.example.core.android.util.ext.setupWithBackNavigation
import com.example.core.android.util.ext.show
import com.example.core.android.view.epoxy.dataListCarouselWithHeader
import com.example.core.android.view.epoxy.injectedTypedController
import com.example.core.android.view.epoxy.pagedDataListCarouselWithHeader
import com.example.spotify.album.R
import com.example.spotify.album.databinding.FragmentSpotifyAlbumBinding
import com.wada811.lifecycledispose.disposeOnDestroy
import org.koin.android.ext.android.inject

class SpotifyAlbumFragment : BaseMvRxFragment() {
    private val factory: ISpotifyFragmentsFactory by inject()
    private val viewModel: SpotifyAlbumViewModel by fragmentViewModel()
    private val album: Album by args()

    private val epoxyController by lazy(LazyThreadSafetyMode.NONE) {
        injectedTypedController<SpotifyAlbumViewState> { (_, artists, tracks) ->
            dataListCarouselWithHeader(
                requireContext(),
                artists,
                R.string.artists,
                "artists",
                viewModel::loadAlbumsArtists
            ) { artist ->
                artist.clickableListItem {
                    show { factory.newSpotifyArtistFragment(artist) }
                }
            }

            pagedDataListCarouselWithHeader(
                requireContext(),
                tracks,
                R.string.tracks,
                "tracks",
                viewModel::loadTracksFromAlbum
            ) { track ->
                TrackPopularityItemBindingModel_()
                    .id(track.id)
                    .track(track) //TODO: navigation + a nicer layout
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
        albumFavouriteFab.setOnClickListener { }
        albumToolbarGradientBackgroundView
            .loadBackgroundGradient(this@SpotifyAlbumFragment.album.iconUrl)
            .disposeOnDestroy(this@SpotifyAlbumFragment)
        albumRecyclerView.setController(epoxyController)
        albumToolbar.setupWithBackNavigation(requireActivity() as? AppCompatActivity)
    }.root

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = false

    override fun invalidate() = withState(viewModel, epoxyController::setData)

    companion object {
        fun new(album: Album): SpotifyAlbumFragment = newFragmentWithMvRxArg(album)
    }
}
