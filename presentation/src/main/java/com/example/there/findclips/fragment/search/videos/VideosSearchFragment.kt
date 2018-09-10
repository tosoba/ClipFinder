package com.example.there.findclips.fragment.search.videos

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
import com.example.there.findclips.base.fragment.BaseVMFragment
import com.example.there.findclips.databinding.FragmentVideosSearchBinding
import com.example.there.findclips.fragment.search.MainSearchFragment
import com.example.there.findclips.lifecycle.ConnectivityComponent
import com.example.there.findclips.lifecycle.DisposablesComponent
import com.example.there.findclips.model.entity.VideoPlaylist
import com.example.there.findclips.util.ext.mainActivity
import com.example.there.findclips.util.ext.screenOrientation
import com.example.there.findclips.view.list.impl.VideosList
import com.example.there.findclips.view.recycler.EndlessRecyclerOnScrollListener
import com.example.there.findclips.view.recycler.SeparatorDecoration
import kotlinx.android.synthetic.main.fragment_videos_search.*


class VideosSearchFragment : BaseVMFragment<VideosSearchViewModel>(), MainSearchFragment {

    override var query: String = ""
        set(value) {
            if (field == value) return
            field = value
            loadData()
            viewModel.viewState.videos.clear()
        }

    private val onScrollListener: RecyclerView.OnScrollListener = object : EndlessRecyclerOnScrollListener() {
        override fun onLoadMore() = viewModel.searchVideosWithLastQuery()
    }

    private val videosAdapter: VideosList.Adapter by lazy {
        VideosList.Adapter(viewModel.viewState.videos, R.layout.video_item)
    }

    private val view: VideosSearchView by lazy {
        VideosSearchView(
                state = viewModel.viewState,
                videosAdapter = videosAdapter,
                onScrollListener = onScrollListener,
                videosItemDecoration = SeparatorDecoration(context!!, ResourcesCompat.getColor(resources, R.color.colorAccent, null), 2f)
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentVideosSearchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_videos_search, container, false)
        binding.videosSearchView = view
        binding.videosRecyclerView.layoutManager = videosLayoutManager
        return binding.root
    }

    private val disposablesComponent = DisposablesComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(disposablesComponent)
        initItemClicks()

        initFromArguments()
    }

    private fun initItemClicks() {
        disposablesComponent.add(videosAdapter.itemClicked.subscribe {
            mainActivity?.loadVideo(video = it)
        })
    }

    private fun initFromArguments() {
        arguments?.let {
            if (it.containsKey(ARG_QUERY)) {
                query = it.getString(ARG_QUERY)!!
            } else if (it.containsKey(ARG_VIDEO_PLAYLIST)) {
                val videoPlaylist = it.getParcelable<VideoPlaylist>(ARG_VIDEO_PLAYLIST)!!
                viewModel.getFavouriteVideosFromPlaylist(videoPlaylist)
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        updateRecyclerViewOnConfigChange()
    }

    private val videosLayoutManager: RecyclerView.LayoutManager
        get() = if (context?.screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        } else {
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

    private fun updateRecyclerViewOnConfigChange() {
        videos_recycler_view?.adapter = null
        videos_recycler_view?.layoutManager = videosLayoutManager
        videos_recycler_view?.adapter = videosAdapter
        videosAdapter.notifyDataSetChanged()
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(VideosSearchViewModel::class.java)
    }

    private val connectivityComponent: ConnectivityComponent by lazy {
        ConnectivityComponent(
                activity!!,
                { query == "" || viewModel.viewState.videos.isNotEmpty() },
                mainActivity!!.connectivitySnackbarParentView!!,
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
