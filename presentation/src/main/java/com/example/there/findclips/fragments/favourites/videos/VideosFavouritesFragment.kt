package com.example.there.findclips.fragments.favourites.videos

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.Router
import com.example.there.findclips.databinding.FragmentVideosFavouritesBinding
import com.example.there.findclips.model.entities.VideoPlaylist
import com.example.there.findclips.view.lists.OnVideoPlaylistClickListener
import com.example.there.findclips.view.lists.VideoPlaylistsList
import com.example.there.findclips.view.recycler.SeparatorDecoration


class VideosFavouritesFragment : Fragment() {

    val state: VideosFavouritesFragmentViewState = VideosFavouritesFragmentViewState()

    fun updateState(playlists: List<VideoPlaylist>) {
        state.playlists.clear()
        state.playlists.addAll(playlists)
    }

    private val onPlaylistClickListener = object : OnVideoPlaylistClickListener {
        override fun onClick(item: VideoPlaylist) = Router.goToVideoPlaylistActivity(activity, videoPlaylist = item)
    }

    private val view: VideosFavouritesFragmentView by lazy {
        VideosFavouritesFragmentView(state = state,
                playlistsAdapter = VideoPlaylistsList.Adapter(state.playlists, R.layout.video_playlist_item, onPlaylistClickListener),
                itemDecoration = SeparatorDecoration(context!!, ResourcesCompat.getColor(resources, R.color.colorAccent, null), 2f))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentVideosFavouritesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_videos_favourites, container, false)
        return binding.apply {
            this.view = this@VideosFavouritesFragment.view
            videosFavouritesPlaylistsRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }.root
    }
}
