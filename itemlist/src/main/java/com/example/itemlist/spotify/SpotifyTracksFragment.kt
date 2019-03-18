package com.example.itemlist.spotify

import android.support.v4.app.Fragment
import com.example.coreandroid.base.fragment.BaseListFragment
import com.example.coreandroid.model.spotify.Track
import com.example.coreandroid.util.list.IdentifiableObservableListItem
import com.example.coreandroid.util.list.ObservableSortedList
import com.example.coreandroid.view.recyclerview.binder.ItemBinder
import com.example.coreandroid.view.recyclerview.binder.ItemBinderBase
import com.example.coreandroid.view.recyclerview.item.ListItemView
import com.example.coreandroid.R
import com.example.there.findclips.spotify.trackvideos.TrackVideosFragment


class SpotifyTracksFragment : BaseListFragment<Track>() {

    override val defaultHeaderText: String = "Tracks"

    override val viewState: ViewState<Track> = ViewState(ObservableSortedList<Track>(Track::class.java, IdentifiableObservableListItem.unsortedCallback()))

    override val listItemView: ListItemView<Track>
        get() = object : ListItemView<Track>(viewState.items) {
            override val itemViewBinder: ItemBinder<Track>
                get() = ItemBinderBase(BR.imageListItem, R.layout.named_image_grid_list_item)
        }

    override fun fragmentToShowOnItemClick(item: Track): Fragment = TrackVideosFragment.newInstance(item)
}
