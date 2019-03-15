package com.example.there.findclips.spotify.list

import android.support.v4.app.Fragment
import com.android.databinding.library.baseAdapters.BR
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseListFragment
import com.example.there.findclips.model.entity.spotify.Artist
import com.example.there.findclips.spotify.spotifyitem.artist.ArtistFragment
import com.example.there.findclips.util.list.IdentifiableObservableListItem
import com.example.there.findclips.util.list.ObservableSortedList
import com.example.there.findclips.view.list.binder.ItemBinder
import com.example.there.findclips.view.list.binder.ItemBinderBase
import com.example.there.findclips.view.list.item.ListItemView


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
    ): Fragment = ArtistFragment.newInstance(item)
}
