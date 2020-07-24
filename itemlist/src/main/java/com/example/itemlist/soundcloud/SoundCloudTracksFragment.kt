package com.example.itemlist.soundcloud

import androidx.fragment.app.Fragment
import com.example.core.android.BR
import com.example.core.android.R
import com.example.core.android.base.fragment.BaseListFragment
import com.example.core.android.model.soundcloud.SoundCloudTrack
import com.example.core.android.util.list.IdentifiableNamedObservableListItem
import com.example.core.android.util.list.ObservableSortedList
import com.example.core.android.view.recyclerview.binder.ItemBinder
import com.example.core.android.view.recyclerview.binder.ItemBinderBase
import com.example.core.android.view.recyclerview.item.ListItemView

class SoundCloudTracksFragment : BaseListFragment<SoundCloudTrack>() {

    override val defaultHeaderText: String = "Tracks"

    override val viewState: ViewState<SoundCloudTrack> = ViewState(
        ObservableSortedList(
            SoundCloudTrack::class.java,
            IdentifiableNamedObservableListItem.sortedByNameCallback()
        )
    )

    override val listItemView: ListItemView<SoundCloudTrack>
        get() = object : ListItemView<SoundCloudTrack>(viewState.items) {
            override val itemViewBinder: ItemBinder<SoundCloudTrack>
                get() = ItemBinderBase(BR.imageListItem, R.layout.named_image_grid_list_item)
        }

    override fun fragmentToShowOnItemClick(item: SoundCloudTrack): Fragment = fragmentFactory
        .newSoundCloudTrackVideosFragment(track = item)
}
