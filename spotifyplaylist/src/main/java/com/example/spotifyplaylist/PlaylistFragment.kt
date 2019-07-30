package com.example.spotifyplaylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.coreandroid.base.fragment.BaseVMFragment
import com.example.coreandroid.base.playlist.PlaylistView
import com.example.coreandroid.lifecycle.ConnectivityComponent
import com.example.coreandroid.lifecycle.DisposablesComponent
import com.example.coreandroid.lifecycle.OnPropertyChangedCallbackComponent
import com.example.coreandroid.model.spotify.Playlist
import com.example.coreandroid.util.ext.*
import com.example.itemlist.spotify.SpotifyTracksFragment
import com.example.spotifyplaylist.databinding.FragmentPlaylistBinding

class PlaylistFragment : BaseVMFragment<PlaylistViewModel>(PlaylistViewModel::class) {

    private val playlist: Playlist by lazy { arguments!!.getParcelable<Playlist>(ARG_PLAYLIST) }

    private val view: PlaylistView<Playlist> by lazy {
        PlaylistView(
                state = viewModel.viewState,
                playlist = playlist,
                onFavouriteBtnClickListener = View.OnClickListener {
                    if (viewModel.viewState.isSavedAsFavourite.get() == true) {
                        viewModel.deleteFavouritePlaylist(playlist)
                        Toast.makeText(activity, "${playlist.name} deleted from favourite playlists.", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.addFavouritePlaylist(playlist)
                        Toast.makeText(activity, "${playlist.name} added to favourite playlists.", Toast.LENGTH_SHORT).show()
                    }
                }
        )
    }

    private val connectivityComponent: ConnectivityComponent by lazy {
        reloadingConnectivityComponent(::loadData) { viewModel.tracks.value == null }
    }

    private val tracksFragment: SpotifyTracksFragment
        get() = childFragmentManager.findFragmentById(R.id.playlist_spotify_tracks_fragment) as SpotifyTracksFragment

    private val disposablesComponent = DisposablesComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        lifecycle.addObserver(disposablesComponent)
        loadData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentPlaylistBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_playlist, container, false)
        lifecycle.addObserver(OnPropertyChangedCallbackComponent(viewModel.viewState.isSavedAsFavourite) { _, _ ->
            binding.playlistFavouriteFab.hideAndShow()
        })
        enableSpotifyPlayButton { loadPlaylist(playlist) }
        return binding.apply {
            view = this@PlaylistFragment.view
            playlistToolbarGradientBackgroundView.loadBackgroundGradient(playlist.iconUrl, disposablesComponent)
            playlistToolbar.setupWithBackNavigation(appCompatActivity)
        }.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(connectivityComponent)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tracksFragment.loadMore = ::loadData
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.tracks.observe(this, Observer { tracks ->
            tracks?.let { tracksFragment.updateItems(it, false) }
        })
    }

    private fun loadData() = viewModel.loadTracks(playlist)

    companion object {
        private const val ARG_PLAYLIST = "ARG_PLAYLIST"

        fun newInstance(playlist: Playlist) = PlaylistFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_PLAYLIST, playlist)
            }
        }
    }
}