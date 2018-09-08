package com.example.there.findclips.fragment.favourites.videos

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R

import com.example.there.findclips.base.fragment.BaseVMFragment
import com.example.there.findclips.databinding.FragmentVideosFavouritesBinding
import com.example.there.findclips.di.Injectable
import com.example.there.findclips.fragment.search.videos.VideosSearchFragment
import com.example.there.findclips.model.entity.VideoPlaylist
import com.example.there.findclips.util.ext.hostFragment
import com.example.there.findclips.view.list.OnVideoPlaylistClickListener
import com.example.there.findclips.view.list.VideoPlaylistsList
import com.example.there.findclips.view.recycler.SeparatorDecoration


class VideosFavouritesFragment : BaseVMFragment<VideosFavouritesViewModel>(), Injectable {

    private val onPlaylistClickListener = object : OnVideoPlaylistClickListener {
        override fun onClick(item: VideoPlaylist) {
            hostFragment?.showFragment(VideosSearchFragment.newInstanceWithVideoPlaylist(videoPlaylist = item), true)
        }
    }

    private val view: VideosFavouritesFragmentView by lazy {
        VideosFavouritesFragmentView(
                state = viewModel.state,
                playlistsAdapter = VideoPlaylistsList.Adapter(viewModel.state.playlists, R.layout.video_playlist_item, onPlaylistClickListener),
                itemDecoration = SeparatorDecoration(context!!, ResourcesCompat.getColor(resources, R.color.colorAccent, null), 2f)
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentVideosFavouritesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_videos_favourites, container, false)
        return binding.apply {
            this.view = this@VideosFavouritesFragment.view
            videosFavouritesPlaylistsRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadVideoPlaylists()
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(VideosFavouritesViewModel::class.java)
    }
}
