package com.example.youtubefavourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coreandroid.BR
import com.example.coreandroid.base.IFragmentFactory
import com.example.coreandroid.base.fragment.BaseVMFragment
import com.example.coreandroid.databinding.HeaderItemBinding
import com.example.coreandroid.util.ext.navHostFragment
import com.example.coreandroid.view.recyclerview.binder.ItemBinder
import com.example.coreandroid.view.recyclerview.binder.ItemBinderBase
import com.example.coreandroid.view.recyclerview.decoration.HeaderDecoration
import com.example.coreandroid.view.recyclerview.decoration.SeparatorDecoration
import com.example.coreandroid.view.recyclerview.item.ListItemView
import com.example.coreandroid.view.recyclerview.item.RecyclerViewItemView
import com.example.coreandroid.view.recyclerview.item.RecyclerViewItemViewState
import com.example.coreandroid.view.recyclerview.listener.ClickHandler
import com.example.coreandroid.view.viewflipper.PlaylistThumbnailView
import com.example.youtubefavourites.databinding.FragmentVideosFavouritesBinding
import org.koin.android.ext.android.inject

class VideosFavouritesFragment :
        BaseVMFragment<VideosFavouritesViewModel>(VideosFavouritesViewModel::class) {

    private val fragmentFactory: IFragmentFactory by inject()

    private val playlistsRecyclerViewItemView: RecyclerViewItemView<PlaylistThumbnailView> by lazy {
        RecyclerViewItemView(
                RecyclerViewItemViewState(
                        ObservableField(false), viewModel.state.playlists, ObservableField(false)),
                object : ListItemView<PlaylistThumbnailView>(viewModel.state.playlists) {
                    override val itemViewBinder: ItemBinder<PlaylistThumbnailView>
                        get() = ItemBinderBase(BR.view, R.layout.video_thumbnails_playlist_item)
                },
                ClickHandler {
                    navHostFragment?.showFragment(fragmentFactory.newVideoPlaylistFragment(it.playlist, it.adapter.thumbnailUrls.toTypedArray()), true)
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

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentVideosFavouritesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_videos_favourites, container, false)
        return binding.apply {
            view = this@VideosFavouritesFragment.view
            videosFavouritesPlaylistsRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

            val headerBinding = DataBindingUtil.inflate<HeaderItemBinding>(
                    LayoutInflater.from(context),
                    R.layout.header_item,
                    null,
                    false
            ).apply {
                text = "Playlists"
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
