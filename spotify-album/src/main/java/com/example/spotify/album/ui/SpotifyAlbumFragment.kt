package com.example.spotify.album.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.airbnb.mvrx.*
import com.example.core.android.TrackPopularityItemBindingModel_
import com.example.core.android.base.IFragmentFactory
import com.example.core.android.model.spotify.Album
import com.example.core.android.model.spotify.clickableListItem
import com.example.core.android.util.ext.*
import com.example.core.android.view.epoxy.dataListCarouselWithHeader
import com.example.core.android.view.epoxy.injectedTypedController
import com.example.core.android.view.epoxy.pagedDataListCarouselWithHeader
import com.example.spotify.album.R
import com.example.spotify.album.databinding.FragmentSpotifyAlbumBinding
import com.wada811.lifecycledispose.disposeOnDestroy
import kotlinx.android.synthetic.main.fragment_spotify_album.*
import org.koin.android.ext.android.inject

class SpotifyAlbumFragment : BaseMvRxFragment() {

    private val factory: IFragmentFactory by inject()

    private val viewModel: SpotifyAlbumViewModel by fragmentViewModel()

    private val album: Album by args()

    private val epoxyController by lazy(LazyThreadSafetyMode.NONE) {
        injectedTypedController<SpotifyAlbumViewState> { (album, artists, tracks) ->
            dataListCarouselWithHeader(
                requireContext(),
                artists,
                R.string.artists,
                "artists",
                { viewModel.loadAlbumsArtists(album.artists.map { it.id }) }
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
                { viewModel.loadTracksFromAlbum(album.id) }
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
