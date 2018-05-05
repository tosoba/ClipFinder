package com.example.there.findclips.listfragments

import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.Router
import com.example.there.findclips.databinding.FragmentSpotifyTracksBinding
import com.example.there.findclips.entities.Track
import com.example.there.findclips.lists.GridTracksList
import com.example.there.findclips.util.SeparatorDecoration
import com.example.there.findclips.util.screenOrientation


class SpotifyTracksFragment : BaseSpotifyFragment<Track>() {

    override val viewState: BaseSpotifyFragment.ViewState<Track> = BaseSpotifyFragment.ViewState()

    private val onTrackClickListener = object : GridTracksList.OnItemClickListener {
        override fun onClick(item: Track) {
            Router.goToTrackVideosActivity(activity, track = item)
        }
    }

    private val view: SpotifyTracksFragment.View by lazy {
        SpotifyTracksFragment.View(
                state = viewState,
                adapter = GridTracksList.Adapter(viewState.items, R.layout.grid_track_item, onTrackClickListener),
                itemDecoration = SeparatorDecoration(context!!, context!!.resources.getColor(R.color.colorAccent), 2f)
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        val binding: FragmentSpotifyTracksBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_spotify_tracks, container, false)
        binding.view = view
        binding.tracksRecyclerView.layoutManager = if (activity?.screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        } else {
            GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }
        return binding.root
    }

    data class View(val state: BaseSpotifyFragment.ViewState<Track>,
                    val adapter: GridTracksList.Adapter,
                    val itemDecoration: RecyclerView.ItemDecoration)
}
