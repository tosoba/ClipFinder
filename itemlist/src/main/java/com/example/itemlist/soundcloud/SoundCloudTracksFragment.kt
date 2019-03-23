package com.example.itemlist.soundcloud

import android.support.v4.app.Fragment
import com.example.coreandroid.BR
import com.example.coreandroid.R
import com.example.coreandroid.base.fragment.BaseListFragment
import com.example.coreandroid.model.soundcloud.SoundCloudTrack
import com.example.coreandroid.util.list.IdentifiableNamedObservableListItem
import com.example.coreandroid.util.list.ObservableSortedList
import com.example.coreandroid.view.recyclerview.binder.ItemBinder
import com.example.coreandroid.view.recyclerview.binder.ItemBinderBase
import com.example.coreandroid.view.recyclerview.item.ListItemView


class SoundCloudTracksFragment : BaseListFragment<SoundCloudTrack>() {

    override val defaultHeaderText: String = "Tracks"

    override val viewState: ViewState<SoundCloudTrack> = ViewState(
            ObservableSortedList(SoundCloudTrack::class.java, IdentifiableNamedObservableListItem.sortedByNameCallback())
    )

    override val listItemView: ListItemView<SoundCloudTrack>
        get() = object : ListItemView<SoundCloudTrack>(viewState.items) {
            override val itemViewBinder: ItemBinder<SoundCloudTrack>
                get() = ItemBinderBase(BR.imageListItem, R.layout.named_image_grid_list_item)
        }

    override fun fragmentToShowOnItemClick(item: SoundCloudTrack): Fragment = fragmentFactory.newSoundCloudTrackVideosFragment(track = item)
}
