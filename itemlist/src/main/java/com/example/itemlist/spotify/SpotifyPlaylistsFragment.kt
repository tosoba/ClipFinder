package com.example.itemlist.spotify

import android.support.v4.app.Fragment
import com.example.coreandroid.BR
import com.example.coreandroid.R
import com.example.coreandroid.base.fragment.BaseListFragment
import com.example.coreandroid.model.spotify.Playlist
import com.example.coreandroid.util.list.IdentifiableObservableListItem
import com.example.coreandroid.util.list.ObservableSortedList
import com.example.coreandroid.view.recyclerview.binder.ItemBinder
import com.example.coreandroid.view.recyclerview.binder.ItemBinderBase
import com.example.coreandroid.view.recyclerview.item.ListItemView


class SpotifyPlaylistsFragment : BaseListFragment<Playlist>() {

    override val defaultHeaderText: String = "Playlists"

    override val viewState: ViewState<Playlist> = ViewState(
            ObservableSortedList<Playlist>(Playlist::class.java, IdentifiableObservableListItem.unsortedCallback())
    )

    override val listItemView: ListItemView<Playlist>
        get() = object : ListItemView<Playlist>(viewState.items) {
            override val itemViewBinder: ItemBinder<Playlist>
                get() = ItemBinderBase(BR.playlist, R.layout.grid_playlist_item)
        }

    override fun fragmentToShowOnItemClick(item: Playlist): Fragment = fragmentFactory.newSpotifyPlaylistFragment(item)
}
