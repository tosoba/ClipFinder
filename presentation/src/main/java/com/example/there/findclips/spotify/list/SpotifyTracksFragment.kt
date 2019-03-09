package com.example.there.findclips.spotify.list

import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.databinding.library.baseAdapters.BR
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseSpotifyListFragment
import com.example.there.findclips.databinding.FragmentSpotifyTracksBinding
import com.example.there.findclips.model.entity.spotify.Track
import com.example.there.findclips.spotify.trackvideos.TrackVideosFragment
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.util.ext.navHostFragment
import com.example.there.findclips.util.ext.putArguments
import com.example.there.findclips.view.list.ClickHandler
import com.example.there.findclips.view.list.binder.ItemBinder
import com.example.there.findclips.view.list.binder.ItemBinderBase
import com.example.there.findclips.view.list.item.ListItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemViewState
import kotlinx.android.synthetic.main.fragment_spotify_tracks.*


class SpotifyTracksFragment : BaseSpotifyListFragment<Track>() {

    override val itemsRecyclerView: RecyclerView?
        get() = tracks_recycler_view

    override val defaultHeaderText: String = "Tracks"

    override val viewState: ViewState<Track> = ViewState(ObservableSortedList<Track>(Track::class.java, Track.unsortedListCallback))

    override val view: BaseSpotifyListFragment.View<Track> by lazy {
        BaseSpotifyListFragment.View(
                state = viewState,
                recyclerViewItemView = RecyclerViewItemView(
                        RecyclerViewItemViewState(
                                ObservableField(false),
                                viewState.items,
                                ObservableField(false)
                        ),
                        object : ListItemView<Track>(viewState.items) {
                            override val itemViewBinder: ItemBinder<Track>
                                get() = ItemBinderBase(BR.track, R.layout.grid_track_item)
                        },
                        ClickHandler { track ->
                            onItemClick?.let { it(track) }
                                    ?: run { navHostFragment?.showFragment(TrackVideosFragment.newInstance(track), true) }
                        },
                        null,
                        onScrollListener
                )
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        val binding: FragmentSpotifyTracksBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_spotify_tracks, container, false)
        return binding.apply {
            view = this@SpotifyTracksFragment.view
            tracksRecyclerView.layoutManager = GridLayoutManager(context, listColumnCount, GridLayoutManager.VERTICAL, false)
            if (viewState.shouldShowHeader) tracksRecyclerView.addItemDecoration(headerItemDecoration())
        }.root
    }

    class View(
            val state: BaseSpotifyListFragment.ViewState<Track>,
            val recyclerViewItemView: RecyclerViewItemView<Track>
    )

    companion object {
        fun newInstance(
                mainHintText: String,
                additionalHintText: String,
                items: ArrayList<Track>?,
                shouldShowHeader: Boolean = false
        ) = SpotifyTracksFragment().apply {
            putArguments(mainHintText, additionalHintText, items, shouldShowHeader)
        }
    }
}
