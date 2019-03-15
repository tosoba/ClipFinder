package com.example.there.findclips.soundcloud.list

import android.support.v4.app.Fragment
import com.android.databinding.library.baseAdapters.BR
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseListFragment
import com.example.there.findclips.model.entity.soundcloud.SoundCloudTrack
import com.example.there.findclips.soundcloud.trackvideos.SoundCloudTrackVideosFragment
import com.example.there.findclips.util.list.IdentifiableNamedObservableListItem
import com.example.there.findclips.util.list.ObservableSortedList
import com.example.there.findclips.view.list.binder.ItemBinder
import com.example.there.findclips.view.list.binder.ItemBinderBase
import com.example.there.findclips.view.list.item.ListItemView


class SoundCloudTracksFragment : BaseListFragment<SoundCloudTrack>() {

    override val defaultHeaderText: String = "Tracks"

    override val viewState: ViewState<SoundCloudTrack> = ViewState(ObservableSortedList(SoundCloudTrack::class.java, IdentifiableNamedObservableListItem.sortedByNameCallback()))

    override val listItemView: ListItemView<SoundCloudTrack>
        get() = object : ListItemView<SoundCloudTrack>(viewState.items) {
            override val itemViewBinder: ItemBinder<SoundCloudTrack>
                get() = ItemBinderBase(BR.imageListItem, R.layout.named_image_grid_list_item)
        }

    override fun fragmentToShowOnItemClick(item: SoundCloudTrack): Fragment = SoundCloudTrackVideosFragment.newInstance(item)
}
