package com.clipfinder.itemlist.spotify

import androidx.fragment.app.Fragment
import com.clipfinder.core.android.BR
import com.clipfinder.core.android.R
import com.clipfinder.core.android.base.fragment.BaseListFragment
import com.clipfinder.core.android.spotify.model.Track
import com.clipfinder.core.android.spotify.navigation.ISpotifyFragmentsFactory
import com.clipfinder.core.android.view.recyclerview.binder.ItemBinder
import com.clipfinder.core.android.view.recyclerview.binder.ItemBinderBase
import com.clipfinder.core.android.view.recyclerview.item.ListItemView
import org.koin.android.ext.android.inject

class SpotifyTracksFragment : BaseListFragment<Track>() {
    private val fragmentFactory: ISpotifyFragmentsFactory by inject()
    override val defaultHeaderText: String = "Tracks"
    override val viewState: ViewState<Track> = ViewState()

    override val listItemView: ListItemView<Track>
        get() = object : ListItemView<Track>(viewState.items) {
            override val itemViewBinder: _root_ide_package_.com.clipfinder.core.android.view.recyclerview.binder.ItemBinder<Track>
                get() = _root_ide_package_.com.clipfinder.core.android.view.recyclerview.binder.ItemBinderBase(BR.imageListItem, R.layout.named_image_grid_list_item)
        }

    override fun fragmentToShowOnItemClick(item: Track): Fragment = fragmentFactory
        .newSpotifyTrackVideosFragment(item)
}
