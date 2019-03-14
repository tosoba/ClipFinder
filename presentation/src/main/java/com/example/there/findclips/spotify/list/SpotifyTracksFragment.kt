package com.example.there.findclips.spotify.list

import android.support.v4.app.Fragment
import com.android.databinding.library.baseAdapters.BR
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseListFragment
import com.example.there.findclips.model.entity.spotify.Track
import com.example.there.findclips.spotify.trackvideos.TrackVideosFragment
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.view.list.binder.ItemBinder
import com.example.there.findclips.view.list.binder.ItemBinderBase
import com.example.there.findclips.view.list.item.ListItemView


class SpotifyTracksFragment : BaseListFragment<Track>() {

    override val defaultHeaderText: String = "Tracks"

    override val viewState: ViewState<Track> = ViewState(ObservableSortedList<Track>(Track::class.java, Track.unsortedListCallback))

    override val listItemView: ListItemView<Track>
        get() = object : ListItemView<Track>(viewState.items) {
            override val itemViewBinder: ItemBinder<Track>
                get() = ItemBinderBase(BR.track, R.layout.grid_track_item)
        }

    override fun newInstanceOfFragmentToShowOnClick(
            item: Track
    ): Fragment = TrackVideosFragment.newInstance(item)
}
