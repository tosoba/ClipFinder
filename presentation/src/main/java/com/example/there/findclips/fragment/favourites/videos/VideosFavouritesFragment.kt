package com.example.there.findclips.fragment.favourites.videos

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.databinding.library.baseAdapters.BR
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseVMFragment
import com.example.there.findclips.databinding.FragmentVideosFavouritesBinding
import com.example.there.findclips.di.Injectable
import com.example.there.findclips.fragment.search.videos.VideosSearchFragment
import com.example.there.findclips.model.entity.VideoPlaylist
import com.example.there.findclips.util.ext.hostFragment
import com.example.there.findclips.view.list.ClickHandler
import com.example.there.findclips.view.list.binder.ItemBinder
import com.example.there.findclips.view.list.binder.ItemBinderBase
import com.example.there.findclips.view.list.item.ListItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemViewState
import com.example.there.findclips.view.recycler.SeparatorDecoration


class VideosFavouritesFragment : BaseVMFragment<VideosFavouritesViewModel>(), Injectable {

    private val playlistsRecyclerViewItemView: RecyclerViewItemView<VideoPlaylist> by lazy {
        RecyclerViewItemView(
                RecyclerViewItemViewState(
                        ObservableField(false),
                        viewModel.state.playlists
                ),
                object : ListItemView<VideoPlaylist>(viewModel.state.playlists) {
                    override val itemViewBinder: ItemBinder<VideoPlaylist>
                        get() = ItemBinderBase(BR.playlist, R.layout.video_playlist_item)
                },
                ClickHandler {
                    hostFragment?.showFragment(VideosSearchFragment.newInstanceWithVideoPlaylist(videoPlaylist = it), true)
                },
                SeparatorDecoration(context!!, ResourcesCompat.getColor(resources, R.color.colorAccent, null), 2f),
                null
        )
    }

    private val view: VideosFavouritesFragmentView by lazy {
        VideosFavouritesFragmentView(
                state = viewModel.state,
                playlistsRecyclerViewItemView = playlistsRecyclerViewItemView
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
