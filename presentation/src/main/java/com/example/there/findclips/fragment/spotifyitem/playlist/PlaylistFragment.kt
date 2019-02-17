package com.example.there.findclips.fragment.spotifyitem.playlist

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseVMFragment
import com.example.there.findclips.databinding.FragmentPlaylistBinding
import com.example.there.findclips.di.Injectable
import com.example.there.findclips.fragment.list.SpotifyTracksFragment
import com.example.there.findclips.lifecycle.ConnectivityComponent
import com.example.there.findclips.lifecycle.DisposablesComponent
import com.example.there.findclips.lifecycle.OnPropertyChangedCallbackComponent
import com.example.there.findclips.model.entity.Playlist
import com.example.there.findclips.util.ext.*
import com.squareup.picasso.Picasso

class PlaylistFragment : BaseVMFragment<PlaylistViewModel>(PlaylistViewModel::class.java), Injectable {

    private val playlist: Playlist by lazy { arguments!!.getParcelable<Playlist>(ARG_PLAYLIST) }

    private val view: PlaylistView by lazy {
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
                },
                onPlayBtnClickListener = View.OnClickListener {
                    val playPlaylist: () -> Unit = { spotifyPlayerController?.loadPlaylist(playlist) }
                    if (spotifyPlayerController?.isPlayerLoggedIn == true) {
                        playPlaylist()
                    } else {
                        spotifyLoginController?.showLoginDialog()
                        spotifyLoginController?.onLoginSuccessful = playPlaylist
                    }
                }
        )
    }

    private val connectivityComponent: ConnectivityComponent by lazy {
        ConnectivityComponent(
                activity!!,
                { viewModel.tracks.value != null },
                connectivitySnackbarHost!!.connectivitySnackbarParentView!!,
                ::loadData,
                true
        )
    }

    private val tracksFragment: SpotifyTracksFragment
        get() = childFragmentManager.findFragmentById(R.id.playlist_spotify_tracks_fragment) as SpotifyTracksFragment

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(connectivityComponent)
    }

    private val disposablesComponent = DisposablesComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        lifecycle.addObserver(disposablesComponent)
        loadData()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentPlaylistBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_playlist, container, false)
        lifecycle.addObserver(OnPropertyChangedCallbackComponent(viewModel.viewState.isSavedAsFavourite) { _, _ ->
            binding.playPlaylistFab.hideAndShow()
        })
        return binding.apply {
            view = this@PlaylistFragment.view
            disposablesComponent.add(Picasso.with(context).getBitmapSingle(playlist.iconUrl, {
                it.generateColorGradient {
                    playlistToolbarGradientBackgroundView.background = it
                    playlistToolbarGradientBackgroundView.invalidate()
                }
            }))
            playlistToolbar.setupWithBackNavigation(appCompatActivity)
        }.root
    }

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

    private fun loadData() = viewModel.loadTracks( playlist)

    companion object {
        private const val ARG_PLAYLIST = "ARG_PLAYLIST"

        fun newInstance(playlist: Playlist) = PlaylistFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_PLAYLIST, playlist)
            }
        }
    }
}
