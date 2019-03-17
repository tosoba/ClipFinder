package com.example.there.findclips.spotify.list

import android.support.v4.app.Fragment
import com.example.there.findclips.R
import com.example.there.findclips.spotify.spotifyitem.album.AlbumFragment


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
