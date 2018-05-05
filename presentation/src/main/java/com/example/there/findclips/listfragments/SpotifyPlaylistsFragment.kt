package com.example.there.findclips.listfragments

import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.Router
import com.example.there.findclips.databinding.FragmentSpotifyPlaylistsBinding
import com.example.there.findclips.entities.Playlist
import com.example.there.findclips.lists.GridPlaylistsList
import com.example.there.findclips.util.screenOrientation


class SpotifyPlaylistsFragment : BaseSpotifyFragment<Playlist>() {

    private val onPlaylistClickListener = object : GridPlaylistsList.OnItemClickListener {
        override fun onClick(item: Playlist) {
            Router.goToPlaylistActivity(activity, playlist = item)
        }
    }

    override val viewState: BaseSpotifyFragment.ViewState<Playlist> = BaseSpotifyFragment.ViewState()

    private val view: SpotifyPlaylistsFragment.View = SpotifyPlaylistsFragment.View(
            state = viewState,
            adapter = GridPlaylistsList.Adapter(viewState.items, R.layout.grid_playlist_item, onPlaylistClickListener)
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        val binding: FragmentSpotifyPlaylistsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_spotify_playlists, container, false)
        binding.view = view
        binding.playlistsRecyclerView.layoutManager = if (activity?.screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        } else {
            GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }
        return binding.root
    }

    data class View(val state: BaseSpotifyFragment.ViewState<Playlist>,
                    val adapter: GridPlaylistsList.Adapter)
}
