package com.example.itemlist.spotify

import android.support.v4.app.Fragment
import com.example.coreandroid.R
import com.example.coreandroid.base.fragment.BaseListFragment
import com.example.coreandroid.model.spotify.Album
import com.example.coreandroid.util.list.IdentifiableObservableListItem
import com.example.coreandroid.util.list.ObservableSortedList
import com.example.coreandroid.view.recyclerview.binder.ItemBinder
import com.example.coreandroid.view.recyclerview.binder.ItemBinderBase
import com.example.coreandroid.view.recyclerview.item.ListItemView


class SpotifyAlbumsFragment : BaseListFragment<Album>() {

    override val defaultHeaderText: String = "Albums"

    override val viewState: ViewState<Album> = ViewState(ObservableSortedList<Album>(Album::class.java, IdentifiableObservableListItem.unsortedCallback()))

    override val listItemView: ListItemView<Album>
        get() = object : ListItemView<Album>(viewState.items) {
            override val itemViewBinder: ItemBinder<Album>
                get() = ItemBinderBase(BR.imageListItem, R.layout.named_image_grid_list_item)
        }

    override fun fragmentToShowOnItemClick(
            item: Album
    ): Fragment = AlbumFragment.newInstance(item)
}
