package com.example.itemlist.spotify

import androidx.fragment.app.Fragment
import com.example.core.android.BR
import com.example.core.android.R
import com.example.core.android.base.fragment.BaseListFragment
import com.example.core.android.spotify.model.Album
import com.example.core.android.spotify.navigation.ISpotifyFragmentsFactory
import com.example.core.android.util.list.IdentifiableObservableListItem
import com.example.core.android.util.list.ObservableSortedList
import com.example.core.android.view.recyclerview.binder.ItemBinder
import com.example.core.android.view.recyclerview.binder.ItemBinderBase
import com.example.core.android.view.recyclerview.item.ListItemView
import org.koin.android.ext.android.inject

class SpotifyAlbumsFragment : BaseListFragment<Album>() {

    private val fragmentFactory: ISpotifyFragmentsFactory by inject()

    override val defaultHeaderText: String = "Albums"

    override val viewState: ViewState<Album> = ViewState(
        ObservableSortedList(Album::class.java, IdentifiableObservableListItem.unsortedCallback())
    )

    override val listItemView: ListItemView<Album>
        get() = object : ListItemView<Album>(viewState.items) {
            override val itemViewBinder: ItemBinder<Album>
                get() = ItemBinderBase(BR.imageListItem, R.layout.named_image_grid_list_item)
        }

    override fun fragmentToShowOnItemClick(item: Album): Fragment = fragmentFactory
        .newSpotifyAlbumFragment(album = item)
}
