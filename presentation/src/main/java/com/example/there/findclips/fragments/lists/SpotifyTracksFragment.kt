package com.example.there.findclips.fragments.lists

import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.Router
import com.example.there.findclips.databinding.FragmentSpotifyTracksBinding
import com.example.there.findclips.model.entities.Track
import com.example.there.findclips.util.screenOrientation
import com.example.there.findclips.view.lists.GridTracksList
import com.example.there.findclips.view.lists.OnTrackClickListener
import com.example.there.findclips.view.recycler.HeaderDecoration
import com.example.there.findclips.view.recycler.SeparatorDecoration


class SpotifyTracksFragment : BaseSpotifyFragment<Track>() {

    override val viewState: BaseSpotifyFragment.ViewState<Track> = BaseSpotifyFragment.ViewState()

    private val onTrackClickListener = object : OnTrackClickListener {
        override fun onClick(item: Track) = Router.goToTrackVideosActivity(activity, track = item)
    }

    private val view: SpotifyTracksFragment.View by lazy {
        SpotifyTracksFragment.View(
                state = viewState,
                adapter = GridTracksList.Adapter(viewState.items, R.layout.grid_track_item, onTrackClickListener),
                itemDecoration = SeparatorDecoration(context!!, ResourcesCompat.getColor(resources, R.color.colorAccent, null), 2f)
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        val binding: FragmentSpotifyTracksBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_spotify_tracks, container, false)

        return binding.apply {
            view = this@SpotifyTracksFragment.view
            val columnCount = if (activity?.screenOrientation == Configuration.ORIENTATION_LANDSCAPE) 3 else 2
            tracksRecyclerView.layoutManager = GridLayoutManager(context, columnCount, GridLayoutManager.VERTICAL, false)
            tracksRecyclerView.addItemDecoration(HeaderDecoration.with(context)
                    .inflate(R.layout.tracks_header)
                    .parallax(1f)
                    .dropShadowDp(5)
                    .columns(columnCount)
                    .build())
        }.root
    }

    data class View(val state: BaseSpotifyFragment.ViewState<Track>,
                    val adapter: GridTracksList.Adapter,
                    val itemDecoration: RecyclerView.ItemDecoration)
}
