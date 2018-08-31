package com.example.there.findclips.fragments.search.videos

import android.arch.lifecycle.ViewModelProviders
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.Router
import com.example.there.findclips.base.fragment.BaseVMFragment
import com.example.there.findclips.databinding.FragmentVideosSearchBinding
import com.example.there.findclips.fragments.search.MainSearchFragment
import com.example.there.findclips.lifecycle.ConnectivityComponent
import com.example.there.findclips.model.entities.Video
import com.example.there.findclips.model.entities.VideoPlaylist
import com.example.there.findclips.util.screenOrientation
import com.example.there.findclips.view.lists.OnVideoClickListener
import com.example.there.findclips.view.lists.VideosList
import com.example.there.findclips.view.recycler.EndlessRecyclerOnScrollListener
import com.example.there.findclips.view.recycler.SeparatorDecoration
import kotlinx.android.synthetic.main.fragment_videos_search.*


class VideosSearchFragment : BaseVMFragment<VideosSearchViewModel>(), MainSearchFragment {

    override var query: String = ""
        set(value) {
            if (field == value) return
            field = value
            viewModel.searchVideos(value)
            viewModel.viewState.videos.clear()
        }

    private val videoItemClickListener = object : OnVideoClickListener {
        override fun onClick(item: Video) = Router.goToPlayerActivity(activity, video = item, otherVideos = viewModel.viewState.videos)
    }

    private val onScrollListener: RecyclerView.OnScrollListener = object : EndlessRecyclerOnScrollListener() {
        override fun onLoadMore() = viewModel.searchVideosWithLastQuery()
    }

    private val view: VideosSearchView by lazy {
        VideosSearchView(
                state = viewModel.viewState,
                videosAdapter = VideosList.Adapter(viewModel.viewState.videos, R.layout.video_item, videoItemClickListener),
                onScrollListener = onScrollListener,
                videosItemDecoration = SeparatorDecoration(context!!, ResourcesCompat.getColor(resources, R.color.colorAccent, null), 2f)
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentVideosSearchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_videos_search, container, false)
        binding.videosSearchView = view
        binding.videosRecyclerView.layoutManager = if (context?.screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        } else {
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) initFromArguments()
    }

    private fun initFromArguments() {
        arguments?.let {
            if (it.containsKey(ARG_QUERY)) {
                query = it.getString(ARG_QUERY)
            } else if (it.containsKey(ARG_VIDEO_PLAYLIST)) {
                val videoPlaylist = it.getParcelable<VideoPlaylist>(ARG_VIDEO_PLAYLIST)
                viewModel.getFavouriteVideosFromPlaylist(videoPlaylist)
            }
        }
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(VideosSearchViewModel::class.java)
    }

    private val connectivityComponent: ConnectivityComponent by lazy {
        ConnectivityComponent(
                activity!!,
                query == "" || viewModel.viewState.videos.isNotEmpty(),
                videos_search_root_layout,
                ::loadData,
                true
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(connectivityComponent)
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
