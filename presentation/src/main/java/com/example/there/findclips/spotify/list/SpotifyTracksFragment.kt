package com.example.there.findclips.spotify.list

import android.support.v4.app.Fragment
import com.example.there.findclips.R
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
