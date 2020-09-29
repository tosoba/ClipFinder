package com.example.itemlist.spotify

import androidx.fragment.app.Fragment
import com.example.core.android.spotify.BR
import com.example.core.android.spotify.R
import com.example.core.android.base.fragment.BaseListFragment
import com.example.core.android.spotify.model.Playlist
import com.example.core.android.spotify.navigation.ISpotifyFragmentsFactory
import com.example.core.android.util.list.IdentifiableObservableListItem
import com.example.core.android.util.list.ObservableSortedList
import com.example.core.android.view.recyclerview.binder.ItemBinder
import com.example.core.android.view.recyclerview.binder.ItemBinderBase
import com.example.core.android.view.recyclerview.item.ListItemView
import org.koin.android.ext.android.inject

class SpotifyPlaylistsFragment : BaseListFragment<Playlist>() {

    private val fragmentFactory: ISpotifyFragmentsFactory by inject()

    override val defaultHeaderText: String = "Playlists"

    override val viewState: ViewState<Playlist> = ViewState(
        ObservableSortedList(
            Playlist::class.java,
            IdentifiableObservableListItem.unsortedCallback()
        )
    )

    override val listItemView: ListItemView<Playlist>
        get() = object : ListItemView<Playlist>(viewState.items) {
            override val itemViewBinder: ItemBinder<Playlist>
                get() = ItemBinderBase(BR.playlist, R.layout.grid_playlist_item)
        }

    override fun fragmentToShowOnItemClick(item: Playlist): Fragment = fragmentFactory
        .newSpotifyPlaylistFragment(item)
}
