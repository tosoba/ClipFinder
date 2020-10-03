package com.example.itemlist.spotify

import androidx.fragment.app.Fragment
import com.example.core.android.BR
import com.example.core.android.R
import com.example.core.android.base.fragment.BaseListFragment
import com.example.core.android.spotify.model.Artist
import com.example.core.android.spotify.navigation.ISpotifyFragmentsFactory
import com.example.core.android.util.list.IdentifiableObservableListItem
import com.example.core.android.util.list.ObservableSortedList
import com.example.core.android.view.recyclerview.binder.ItemBinder
import com.example.core.android.view.recyclerview.binder.ItemBinderBase
import com.example.core.android.view.recyclerview.item.ListItemView
import org.koin.android.ext.android.inject

class SpotifyArtistsFragment : BaseListFragment<Artist>() {

    private val fragmentFactory: ISpotifyFragmentsFactory by inject()

    override val defaultHeaderText: String = "Artists"

    override val viewState: ViewState<Artist> = ViewState(
        ObservableSortedList<Artist>(
            Artist::class.java,
            IdentifiableObservableListItem.unsortedCallback()
        )
    )

    override val listItemView: ListItemView<Artist>
        get() = object : ListItemView<Artist>(viewState.items) {
            override val itemViewBinder: ItemBinder<Artist>
                get() = ItemBinderBase(BR.imageListItem, R.layout.named_image_grid_list_item)
        }

    override fun fragmentToShowOnItemClick(
        item: Artist
    ): Fragment = fragmentFactory.newSpotifyArtistFragment(artist = item)
}
