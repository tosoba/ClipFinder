package com.example.there.findclips.fragment.favourites.videos

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
import com.example.there.findclips.databinding.HeaderItemBinding
import com.example.there.findclips.di.Injectable
import com.example.there.findclips.fragment.search.videos.VideosSearchFragment
import com.example.there.findclips.model.entity.VideoPlaylistWithThumbnails
import com.example.there.findclips.util.ext.hostFragment
import com.example.there.findclips.view.list.ClickHandler
import com.example.there.findclips.view.list.binder.ItemBinder
import com.example.there.findclips.view.list.binder.ItemBinderBase
import com.example.there.findclips.view.list.item.HeaderItemViewState
import com.example.there.findclips.view.list.item.ListItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemViewState
import com.example.there.findclips.view.recycler.HeaderDecoration
import com.example.there.findclips.view.recycler.SeparatorDecoration


class VideosFavouritesFragment : BaseVMFragment<VideosFavouritesViewModel>(VideosFavouritesViewModel::class.java), Injectable {

    private val playlistsRecyclerViewItemView: RecyclerViewItemView<VideoPlaylistWithThumbnails> by lazy {
        RecyclerViewItemView(
                RecyclerViewItemViewState(
                        ObservableField(false),
                        viewModel.state.playlists
                ),
                object : ListItemView<VideoPlaylistWithThumbnails>(viewModel.state.playlists) {
                    override val itemViewBinder: ItemBinder<VideoPlaylistWithThumbnails>
                        get() = ItemBinderBase(BR.playlist, R.layout.video_thumbnails_playlist_item)
                },
                ClickHandler {
                    hostFragment?.showFragment(VideosSearchFragment.newInstanceWithVideoPlaylist(videoPlaylist = it.playlist), true)
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
            view = this@VideosFavouritesFragment.view
            videosFavouritesPlaylistsRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

            val headerBinding = DataBindingUtil.inflate<HeaderItemBinding>(
                    LayoutInflater.from(context),
                    R.layout.header_item,
                    null,
                    false
            ).apply {
                viewState = HeaderItemViewState("Playlists")
                executePendingBindings()
            }

            videosFavouritesPlaylistsRecyclerView.addItemDecoration(HeaderDecoration(headerBinding.root, false, 1f, 0f, 1))
        }.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadVideoPlaylists()
    }
}
