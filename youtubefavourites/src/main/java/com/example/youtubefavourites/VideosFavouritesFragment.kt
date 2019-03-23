package com.example.youtubefavourites

import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.coreandroid.BR
import com.example.coreandroid.base.IFragmentFactory
import com.example.coreandroid.base.fragment.BaseVMFragment
import com.example.coreandroid.di.Injectable
import com.example.coreandroid.util.ext.navHostFragment
import com.example.coreandroid.view.recyclerview.binder.ItemBinder
import com.example.coreandroid.view.recyclerview.binder.ItemBinderBase
import com.example.coreandroid.view.recyclerview.decoration.HeaderDecoration
import com.example.coreandroid.view.recyclerview.decoration.SeparatorDecoration
import com.example.coreandroid.view.recyclerview.item.HeaderItemViewState
import com.example.coreandroid.view.recyclerview.item.ListItemView
import com.example.coreandroid.view.recyclerview.item.RecyclerViewItemView
import com.example.coreandroid.view.recyclerview.item.RecyclerViewItemViewState
import com.example.coreandroid.view.recyclerview.listener.ClickHandler
import com.example.coreandroid.view.viewflipper.PlaylistThumbnailView
import javax.inject.Inject

class VideosFavouritesFragment :
        BaseVMFragment<VideosFavouritesViewModel>(VideosFavouritesViewModel::class.java),
        Injectable {

    @Inject
    lateinit var fragmentFactory: IFragmentFactory

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: com.example.youtubefavourites.databinding.FragmentVideosFavouritesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_videos_favourites, container, false)
        return binding.apply {
            view = this@VideosFavouritesFragment.view
            videosFavouritesPlaylistsRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

            val headerBinding = DataBindingUtil.inflate<com.example.coreandroid.databinding.HeaderItemBinding>(
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
