package com.example.there.findclips.fragment.list

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseSpotifyListFragment
import com.example.there.findclips.databinding.FragmentSpotifyTracksBinding
import com.example.there.findclips.fragment.trackvideos.TrackVideosFragment
import com.example.there.findclips.model.entity.Track
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.util.ext.hostFragment
import com.example.there.findclips.util.ext.putArguments
import com.example.there.findclips.view.list.impl.GridTracksList
import com.example.there.findclips.view.recycler.SeparatorDecoration
import kotlinx.android.synthetic.main.fragment_spotify_tracks.*


class SpotifyTracksFragment : BaseSpotifyListFragment<Track>() {

    override val itemsRecyclerView: RecyclerView?
        get() = tracks_recycler_view

    override val recyclerViewHeaderLayout: Int
        get() = R.layout.tracks_header

    override val viewState: ViewState<Track> = ViewState(ObservableSortedList<Track>(Track::class.java, Track.sortedListCallbackName))

    private val tracksAdapter: GridTracksList.Adapter by lazy {
        GridTracksList.Adapter(viewState.items, R.layout.grid_track_item)
    }

    private val view: SpotifyTracksFragment.View by lazy {
        SpotifyTracksFragment.View(
                state = viewState,
                adapter = tracksAdapter,
                itemDecoration = SeparatorDecoration(context!!, ResourcesCompat.getColor(resources, R.color.colorAccent, null), 2f),
                onScrollListener = onScrollListener
        )
    }

    override fun initItemClicks() {
        disposablesComponent.add(tracksAdapter.itemClicked.subscribe {
            hostFragment?.showFragment(TrackVideosFragment.newInstance(track = it), true)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        val binding: FragmentSpotifyTracksBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_spotify_tracks, container, false)

        return binding.apply {
            view = this@SpotifyTracksFragment.view
            tracksRecyclerView.layoutManager = GridLayoutManager(context, listColumnCount, GridLayoutManager.VERTICAL, false)
            if (viewState.shouldShowHeader) tracksRecyclerView.addItemDecoration(headerItemDecoration(R.layout.tracks_header))
        }.root
    }

    data class View(
            val state: BaseSpotifyListFragment.ViewState<Track>,
            val adapter: GridTracksList.Adapter,
            val itemDecoration: RecyclerView.ItemDecoration,
            val onScrollListener: RecyclerView.OnScrollListener
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
