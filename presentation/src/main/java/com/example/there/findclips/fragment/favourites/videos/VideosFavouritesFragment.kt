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
import com.example.there.findclips.lifecycle.DisposablesComponent
import com.example.there.findclips.util.ext.hostFragment
import com.example.there.findclips.view.list.impl.VideoPlaylistsList
import com.example.there.findclips.view.recycler.SeparatorDecoration


class VideosFavouritesFragment : BaseVMFragment<VideosFavouritesViewModel>(), Injectable {

    private val playlistsAdapter: VideoPlaylistsList.Adapter by lazy {
        VideoPlaylistsList.Adapter(viewModel.state.playlists, R.layout.video_playlist_item)
    }

    private val view: VideosFavouritesFragmentView by lazy {
        VideosFavouritesFragmentView(
                state = viewModel.state,
                playlistsAdapter = playlistsAdapter,
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

    private val disposablesComponent = DisposablesComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(disposablesComponent)
        disposablesComponent.add(playlistsAdapter.itemClicked.subscribe {
            hostFragment?.showFragment(VideosSearchFragment.newInstanceWithVideoPlaylist(videoPlaylist = it), true)
        })

        viewModel.loadVideoPlaylists()
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(VideosFavouritesViewModel::class.java)
    }
}
