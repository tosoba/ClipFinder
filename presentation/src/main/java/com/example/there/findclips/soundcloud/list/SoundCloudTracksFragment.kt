package com.example.there.findclips.soundcloud.list

import android.support.v4.app.Fragment
import com.example.there.findclips.R
import com.example.there.findclips.soundcloud.trackvideos.SoundCloudTrackVideosFragment


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
