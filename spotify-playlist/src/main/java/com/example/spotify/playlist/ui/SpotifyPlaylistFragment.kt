package com.example.spotify.playlist.ui

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.mvrx.*
import com.example.core.android.base.IFragmentFactory
import com.example.core.android.base.playlist.PlaylistViewState
import com.example.core.android.di.EpoxyHandlerQualifier
import com.example.core.android.model.spotify.Playlist
import com.example.core.android.model.spotify.Track
import com.example.core.android.model.spotify.clickableListItem
import com.example.core.android.util.ext.*
import com.example.core.android.view.epoxy.itemListController
import com.example.spotify.playlist.R
import com.example.spotify.playlist.databinding.FragmentSpotifyPlaylistBinding
import com.wada811.lifecycledispose.disposeOnDestroy
import kotlinx.android.synthetic.main.fragment_spotify_playlist.*
import org.koin.android.ext.android.inject

class SpotifyPlaylistFragment : BaseMvRxFragment(), NavigationCapable {

    private val playlist: Playlist by args()

    override fun invalidate() = withState(viewModel, epoxyController::setData)

    private val builder by inject<Handler>(EpoxyHandlerQualifier.BUILDER)
    private val differ by inject<Handler>(EpoxyHandlerQualifier.DIFFER)

    private val epoxyController by lazy(LazyThreadSafetyMode.NONE) {
        itemListController(
            builder,
            differ,
            viewModel,
            PlaylistViewState<Playlist, Track>::tracks,
            getString(R.string.tracks),
            loadMore = viewModel::loadTracks,
            reloadClicked = viewModel::loadTracks
        ) {
            it.clickableListItem { show { newSpotifyTrackVideosFragment(it) } }
        }
    }

    override val factory: IFragmentFactory by inject()

    private val viewModel: SpotifyPlaylistViewModel by fragmentViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = DataBindingUtil.inflate<FragmentSpotifyPlaylistBinding>(
        inflater, R.layout.fragment_spotify_playlist, container, false
    ).apply {
        enableSpotifyPlayButton { loadPlaylist(this@SpotifyPlaylistFragment.playlist) }
        playlist = this@SpotifyPlaylistFragment.playlist
        playlistFavouriteFab.setOnClickListener { viewModel.togglePlaylistFavouriteState() }
        playlistToolbarGradientBackgroundView
            .loadBackgroundGradient(this@SpotifyPlaylistFragment.playlist.iconUrl)
            .disposeOnDestroy(this@SpotifyPlaylistFragment)
        spotifyPlaylistRecyclerView.apply {
            setController(epoxyController)
            layoutManager = layoutManagerFor(resources.configuration.orientation)
            setItemSpacingDp(5)
        }
        playlistToolbar.setupWithBackNavigation(requireActivity() as? AppCompatActivity)
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.selectSubscribe(
            this,
            PlaylistViewState<Playlist, Track>::isSavedAsFavourite
        ) {
            playlist_favourite_fab?.setImageDrawable(ContextCompat.getDrawable(view.context,
                if (it.value) R.drawable.delete else R.drawable.favourite))
            playlist_favourite_fab?.hideAndShow()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        spotify_playlist_recycler_view?.layoutManager = layoutManagerFor(newConfig.orientation)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = false

    private fun layoutManagerFor(orientation: Int): RecyclerView.LayoutManager = GridLayoutManager(
        context,
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 3
    )

    companion object {
        fun newInstance(playlist: Playlist) = SpotifyPlaylistFragment().apply {
            arguments = Bundle().apply { putParcelable(MvRx.KEY_ARG, playlist) }
        }
    }
}
