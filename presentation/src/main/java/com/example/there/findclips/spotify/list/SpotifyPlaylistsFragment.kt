package com.example.there.findclips.spotify.list

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.databinding.library.baseAdapters.BR
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseListFragment
import com.example.there.findclips.databinding.FragmentSpotifyPlaylistsBinding
import com.example.there.findclips.model.entity.spotify.Playlist
import com.example.there.findclips.spotify.spotifyitem.playlist.PlaylistFragment
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.view.list.binder.ItemBinder
import com.example.there.findclips.view.list.binder.ItemBinderBase
import com.example.there.findclips.view.list.item.ListItemView
import kotlinx.android.synthetic.main.fragment_spotify_playlists.*


class SpotifyPlaylistsFragment : BaseListFragment<Playlist>() {

    override val itemsRecyclerView: RecyclerView?
        get() = playlists_recycler_view

    override val defaultHeaderText: String = "Playlists"

    override val viewState: ViewState<Playlist> = ViewState(ObservableSortedList<Playlist>(Playlist::class.java, Playlist.unsortedListCallback))

    override val listItemView: ListItemView<Playlist>
        get() = object : ListItemView<Playlist>(viewState.items) {
            override val itemViewBinder: ItemBinder<Playlist>
                get() = ItemBinderBase(BR.playlist, R.layout.grid_playlist_item)
        }

    override fun newInstanceOfFragmentToShowOnClick(
            item: Playlist
    ): Fragment = PlaylistFragment.newInstance(item)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        val binding: FragmentSpotifyPlaylistsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_spotify_playlists, container, false)
        return binding.apply {
            view = this@SpotifyPlaylistsFragment.view
            playlistsRecyclerView.layoutManager = GridLayoutManager(context, listColumnCount, GridLayoutManager.VERTICAL, false)
            if (viewState.shouldShowHeader) playlistsRecyclerView.addItemDecoration(headerItemDecoration())
        }.root
    }
}
