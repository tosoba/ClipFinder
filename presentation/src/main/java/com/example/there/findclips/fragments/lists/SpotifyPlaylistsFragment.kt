package com.example.there.findclips.fragments.lists

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseSpotifyListFragment
import com.example.there.findclips.databinding.FragmentSpotifyPlaylistsBinding
import com.example.there.findclips.model.entities.Playlist
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.util.ext.putArguments
import com.example.there.findclips.view.lists.GridPlaylistsList
import com.example.there.findclips.view.lists.OnPlaylistClickListener
import kotlinx.android.synthetic.main.fragment_spotify_playlists.*


class SpotifyPlaylistsFragment : BaseSpotifyListFragment<Playlist>() {

    override val itemsRecyclerView: RecyclerView?
        get() = playlists_recycler_view

    override val recyclerViewHeaderLayout: Int
        get() = R.layout.playlists_header

    override val viewState: ViewState<Playlist> = ViewState(ObservableSortedList<Playlist>(Playlist::class.java, Playlist.sortedListCallback))

    private val onPlaylistClickListener = object : OnPlaylistClickListener {
        override fun onClick(item: Playlist) {
            // show PlaylistFragment
        }
    }

    private val view: SpotifyPlaylistsFragment.View = SpotifyPlaylistsFragment.View(
            state = viewState,
            adapter = GridPlaylistsList.Adapter(viewState.items, R.layout.grid_playlist_item, onPlaylistClickListener),
            onScrollListener = onScrollListener
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        val binding: FragmentSpotifyPlaylistsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_spotify_playlists, container, false)
        return binding.apply {
            view = this@SpotifyPlaylistsFragment.view
            playlistsRecyclerView.layoutManager = GridLayoutManager(context, listColumnCount, GridLayoutManager.VERTICAL, false)
            if (viewState.shouldShowHeader) playlistsRecyclerView.addItemDecoration(headerItemDecoration(R.layout.playlists_header))
        }.root
    }

    data class View(
            val state: BaseSpotifyListFragment.ViewState<Playlist>,
            val adapter: GridPlaylistsList.Adapter,
            val onScrollListener: RecyclerView.OnScrollListener
    )

    companion object {
        fun newInstance(
                mainHintText: String,
                additionalHintText: String,
                items: ArrayList<Playlist>?,
                shouldShowHeader: Boolean = false
        ) = SpotifyPlaylistsFragment().apply {
            putArguments(mainHintText, additionalHintText, items, shouldShowHeader)
        }
    }
}
