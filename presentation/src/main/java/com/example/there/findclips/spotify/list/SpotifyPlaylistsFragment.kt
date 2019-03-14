package com.example.there.findclips.spotify.list

import android.support.v4.app.Fragment
import com.android.databinding.library.baseAdapters.BR
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseListFragment
import com.example.there.findclips.model.entity.spotify.Playlist
import com.example.there.findclips.spotify.spotifyitem.playlist.PlaylistFragment
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.view.list.binder.ItemBinder
import com.example.there.findclips.view.list.binder.ItemBinderBase
import com.example.there.findclips.view.list.item.ListItemView


class SpotifyPlaylistsFragment : BaseListFragment<Playlist>() {

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
}
