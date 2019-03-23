package com.example.itemlist.spotify

import android.support.v4.app.Fragment
import com.example.coreandroid.BR
import com.example.coreandroid.R
import com.example.coreandroid.base.fragment.BaseListFragment
import com.example.coreandroid.model.spotify.Artist
import com.example.coreandroid.util.list.IdentifiableObservableListItem
import com.example.coreandroid.util.list.ObservableSortedList
import com.example.coreandroid.view.recyclerview.binder.ItemBinder
import com.example.coreandroid.view.recyclerview.binder.ItemBinderBase
import com.example.coreandroid.view.recyclerview.item.ListItemView


class SpotifyArtistsFragment : BaseListFragment<Artist>() {

    override val defaultHeaderText: String = "Artists"

    override val viewState: ViewState<Artist> = ViewState(ObservableSortedList<Artist>(Artist::class.java, IdentifiableObservableListItem.unsortedCallback()))

    override val listItemView: ListItemView<Artist>
        get() = object : ListItemView<Artist>(viewState.items) {
            override val itemViewBinder: ItemBinder<Artist>
                get() = ItemBinderBase(BR.imageListItem, R.layout.named_image_grid_list_item)
        }

    override fun fragmentToShowOnItemClick(
            item: Artist
    ): Fragment = fragmentFactory.newSpotifyArtistFragment(artist = item)
}
