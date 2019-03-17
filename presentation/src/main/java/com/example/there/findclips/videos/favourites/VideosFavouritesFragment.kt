package com.example.there.findclips.videos.favourites

import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.videos.videoplaylist.VideoPlaylistFragment
import com.example.there.findclips.view.recycler.SeparatorDecoration
import com.example.there.findclips.view.viewflipper.PlaylistThumbnailView


class VideosFavouritesFragment : com.example.coreandroid.base.fragment.BaseVMFragment<VideosFavouritesViewModel>(VideosFavouritesViewModel::class.java), com.example.coreandroid.di.Injectable {

    private val playlistsRecyclerViewItemView: RecyclerViewItemView<PlaylistThumbnailView> by lazy {
        RecyclerViewItemView(
                RecyclerViewItemViewState(
                        ObservableField(false),
                        viewModel.state.playlists,
                        ObservableField(false)
                ),
                object : ListItemView<PlaylistThumbnailView>(viewModel.state.playlists) {
                    override val itemViewBinder: ItemBinder<PlaylistThumbnailView>
                        get() = ItemBinderBase(BR.view, R.layout.video_thumbnails_playlist_item)
                },
                ClickHandler {
                    navHostFragment?.showFragment(VideoPlaylistFragment.newInstance(it.playlist, it.adapter.thumbnailUrls.toTypedArray()), true)
                },
                SeparatorDecoration(context!!, ResourcesCompat.getColor(resources, R.color.colorAccent, null), 2f)
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

            videosFavouritesPlaylistsRecyclerView.addItemDecoration(com.example.coreandroid.view.list.decoration.HeaderDecoration(headerBinding.root, false, 1f, 0f, 1))
        }.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadVideoPlaylists()
    }
}
