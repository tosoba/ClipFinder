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
import com.example.coreandroid.BR
import com.example.coreandroid.base.fragment.BaseVMFragment
import com.example.coreandroid.base.fragment.IYoutubeSearchFragment
import com.example.coreandroid.lifecycle.ConnectivityComponent
import com.example.coreandroid.model.videos.Video
import com.example.coreandroid.model.videos.VideoPlaylist
import com.example.coreandroid.util.ext.connectivitySnackbarHost
import com.example.coreandroid.util.ext.screenOrientation
import com.example.coreandroid.util.ext.youtubePlayerController
import com.example.coreandroid.view.recyclerview.binder.ItemBinder
import com.example.coreandroid.view.recyclerview.binder.ItemBinderBase
import com.example.coreandroid.view.recyclerview.decoration.SeparatorDecoration
import com.example.coreandroid.view.recyclerview.item.ListItemView
import com.example.coreandroid.view.recyclerview.item.RecyclerViewItemView
import com.example.coreandroid.view.recyclerview.item.RecyclerViewItemViewState
import com.example.coreandroid.view.recyclerview.item.VideoItemView
import com.example.coreandroid.view.recyclerview.listener.ClickHandler
import com.example.coreandroid.view.recyclerview.listener.EndlessRecyclerOnScrollListener
import com.example.youtubesearch.databinding.FragmentVideosSearchBinding
import kotlinx.android.synthetic.main.fragment_videos_search.*


class VideosSearchFragment :
        BaseVMFragment<VideosSearchViewModel>(VideosSearchViewModel::class),
        IYoutubeSearchFragment {

    override var query: String = ""
        set(value) {
            if (field == value) return
            field = value
            loadData()
            viewModel.viewState.videos.clear()
        }

    override val videosLoaded: Boolean
        get() = viewModel.viewState.videos.isNotEmpty()

    override val videos: List<Video>
        get() = viewModel.viewState.videos.map { it.video }

    private val onScrollListener: RecyclerView.OnScrollListener = object : EndlessRecyclerOnScrollListener() {
        override fun onLoadMore() = viewModel.searchVideosWithLastQuery()
    }

    private val connectivityComponent: ConnectivityComponent by lazy {
        ConnectivityComponent(
                activity!!,
                { query == "" || viewModel.viewState.videos.isNotEmpty() },
                connectivitySnackbarHost!!.connectivitySnackbarParentView!!,
                ::loadData,
                true
        )
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
                            youtubePlayerController?.loadVideo(video = it.video)
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentVideosSearchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_videos_search, container, false)
        return binding.apply {
            videosSearchView = view
            videosRecyclerView.layoutManager = videosLayoutManager
        }.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFromArguments()
    }


    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        updateRecyclerViewOnConfigChange()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(connectivityComponent)
    }

    private fun initFromArguments() = arguments?.let {
        if (it.containsKey(ARG_QUERY)) {
            query = it.getString(ARG_QUERY)!!
        } else if (it.containsKey(ARG_VIDEO_PLAYLIST)) {
            val videoPlaylist = it.getParcelable<VideoPlaylist>(ARG_VIDEO_PLAYLIST)!!
            viewModel.getFavouriteVideosFromPlaylist(videoPlaylist)
        }
    }

    private fun updateRecyclerViewOnConfigChange() {
        val adapter = videos_recycler_view?.adapter
        videos_recycler_view?.adapter = null
        videos_recycler_view?.layoutManager = videosLayoutManager
        videos_recycler_view?.adapter = adapter
        adapter?.notifyDataSetChanged()
    }

    private fun loadData() = viewModel.searchVideos(query)

    companion object {
        private const val ARG_QUERY = "ARG_QUERY"
        private const val ARG_VIDEO_PLAYLIST = "ARG_VIDEO_PLAYLIST"

        fun newInstanceWithQuery(query: String): VideosSearchFragment = VideosSearchFragment().apply {
            arguments = Bundle().apply { putString(ARG_QUERY, query) }
        }

        fun newInstanceWithVideoPlaylist(videoPlaylist: VideoPlaylist): VideosSearchFragment = VideosSearchFragment().apply {
            arguments = Bundle().apply { putParcelable(ARG_VIDEO_PLAYLIST, videoPlaylist) }
        }
    }
}
