package com.example.youtubesearch

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.core.ext.castAs
import com.example.core.android.BR
import com.example.core.android.base.fragment.BaseVMFragment
import com.example.core.android.base.fragment.IYoutubeSearchFragment
import com.example.core.android.base.handler.YoutubePlayerController
import com.example.core.android.lifecycle.ConnectivityComponent
import com.example.core.android.model.videos.Video
import com.example.core.android.model.videos.VideoPlaylist
import com.example.core.android.util.ext.reloadingConnectivityComponent
import com.example.core.android.util.ext.screenOrientation
import com.example.core.android.view.recyclerview.binder.ItemBinder
import com.example.core.android.view.recyclerview.binder.ItemBinderBase
import com.example.core.android.view.recyclerview.decoration.SeparatorDecoration
import com.example.core.android.view.recyclerview.item.ListItemView
import com.example.core.android.view.recyclerview.item.RecyclerViewItemView
import com.example.core.android.view.recyclerview.item.RecyclerViewItemViewState
import com.example.core.android.view.recyclerview.item.VideoItemView
import com.example.core.android.view.recyclerview.listener.ClickHandler
import com.example.core.android.view.recyclerview.listener.EndlessRecyclerOnScrollListener
import com.example.youtubesearch.databinding.FragmentVideosSearchBinding
import kotlinx.android.synthetic.main.fragment_videos_search.*

class VideosSearchFragment :
    BaseVMFragment<VideosSearchViewModel>(VideosSearchViewModel::class),
    IYoutubeSearchFragment {

    override val videosLoaded: Boolean get() = viewModel.viewState.videos.isNotEmpty()

    override val videos: List<Video> get() = viewModel.viewState.videos.map { it.video }

    private val onScrollListener: RecyclerView.OnScrollListener = EndlessRecyclerOnScrollListener {
        viewModel.searchVideosWithLastQuery()
    }

    private val view: VideosSearchView by lazy {
        VideosSearchView(
            state = viewModel.viewState,
            recyclerViewItemView = RecyclerViewItemView(
                RecyclerViewItemViewState(
                    viewModel.viewState.videosLoadingInProgress,
                    viewModel.viewState.videos,
                    viewModel.viewState.videosLoadingErrorOccurred
                ),
                object : ListItemView<VideoItemView>(viewModel.viewState.videos) {
                    override val itemViewBinder: ItemBinder<VideoItemView>
                        get() = ItemBinderBase(BR.videoView, R.layout.video_item)
                },
                ClickHandler {
                    activity?.castAs<YoutubePlayerController>()?.loadVideo(video = it.video)
                },
                SeparatorDecoration(context!!, ResourcesCompat.getColor(resources, R.color.colorAccent, null), 2f),
                onScrollListener
            )
        )
    }

    private val videosLayoutManager: RecyclerView.LayoutManager
        get() = if (context?.screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        } else {
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = FragmentVideosSearchBinding
        .inflate(inflater, container, false)
        .apply {
            videosSearchView = view
            videosRecyclerView.layoutManager = videosLayoutManager
        }
        .root

    override fun onNewQuery(query: String) {
        viewModel.searchVideos(query)
        viewModel.viewState.videos.clear()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updateRecyclerViewOnConfigChange()
    }


    private fun updateRecyclerViewOnConfigChange() {
        val adapter = videos_recycler_view?.adapter
        videos_recycler_view?.adapter = null
        videos_recycler_view?.layoutManager = videosLayoutManager
        videos_recycler_view?.adapter = adapter
        adapter?.notifyDataSetChanged()
    }

    companion object {
        private const val ARG_QUERY = "ARG_QUERY"
        private const val ARG_VIDEO_PLAYLIST = "ARG_VIDEO_PLAYLIST"

        fun newInstanceWithQuery(query: String): VideosSearchFragment = VideosSearchFragment().apply {
            arguments = Bundle().apply { putString(ARG_QUERY, query) }
        }

        fun newInstanceWithVideoPlaylist(
            videoPlaylist: VideoPlaylist
        ): VideosSearchFragment = VideosSearchFragment().apply {
            arguments = Bundle().apply { putParcelable(ARG_VIDEO_PLAYLIST, videoPlaylist) }
        }
    }
}
