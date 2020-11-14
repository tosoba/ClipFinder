package com.clipfinder.spotify.playlist

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.args
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.clipfinder.spotify.playlist.databinding.FragmentSpotifyPlaylistBinding
import com.example.core.android.base.playlist.PlaylistViewState
import com.example.core.android.spotify.ext.enableSpotifyPlayButton
import com.example.core.android.spotify.model.Playlist
import com.example.core.android.spotify.model.Track
import com.example.core.android.spotify.model.clickableListItem
import com.example.core.android.spotify.navigation.ISpotifyFragmentsFactory
import com.example.core.android.util.ext.*
import com.example.core.android.view.epoxy.itemListController
import com.wada811.lifecycledispose.disposeOnDestroy
import kotlinx.android.synthetic.main.fragment_spotify_playlist.*
import org.koin.android.ext.android.inject

class SpotifyPlaylistFragment : BaseMvRxFragment() {
    private val factory: ISpotifyFragmentsFactory by inject()
    private val viewModel: SpotifyPlaylistViewModel by fragmentViewModel()
    private val playlist: Playlist by args()

    private val epoxyController: TypedEpoxyController<PlaylistViewState<Playlist, Track>> by lazy(LazyThreadSafetyMode.NONE) {
        itemListController(
            PlaylistViewState<Playlist, Track>::tracks,
            headerText = getString(R.string.tracks),
            loadMore = viewModel::loadTracks,
            reloadClicked = viewModel::loadTracks
        ) { track ->
            track.clickableListItem {
                show { factory.newSpotifyTrackVideosFragment(track) }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = FragmentSpotifyPlaylistBinding.inflate(inflater, container, false)
        .apply {
            enableSpotifyPlayButton { loadPlaylist(this@SpotifyPlaylistFragment.playlist) }
            playlist = this@SpotifyPlaylistFragment.playlist
            playlistFavouriteFab.setOnClickListener { }
            playlistToolbarGradientBackgroundView
                .loadBackgroundGradient(this@SpotifyPlaylistFragment.playlist.iconUrl)
                .disposeOnDestroy(this@SpotifyPlaylistFragment)
            spotifyPlaylistRecyclerView.apply {
                setController(epoxyController)
                layoutManager = layoutManagerFor(resources.configuration.orientation)
                setItemSpacingDp(5)
            }
            playlistToolbar.setupWithBackNavigation(requireActivity() as? AppCompatActivity)
        }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.selectSubscribe(
            this,
            PlaylistViewState<Playlist, Track>::isSavedAsFavourite
        ) {
            playlist_favourite_fab?.setImageDrawable(
                ContextCompat.getDrawable(view.context,
                    if (it.value) R.drawable.delete else R.drawable.favourite)
            )
            playlist_favourite_fab?.hideAndShow()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        spotify_playlist_recycler_view?.layoutManager = layoutManagerFor(newConfig.orientation)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = false

    override fun invalidate() = withState(viewModel, epoxyController::setData)

    private fun layoutManagerFor(orientation: Int): RecyclerView.LayoutManager = GridLayoutManager(
        context,
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 3
    )

    companion object {
        fun new(playlist: Playlist): SpotifyPlaylistFragment = newMvRxFragmentWith(playlist)
    }
}