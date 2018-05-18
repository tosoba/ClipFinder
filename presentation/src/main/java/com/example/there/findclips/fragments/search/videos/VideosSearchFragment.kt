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
import com.example.there.findclips.base.BaseVMFragment
import com.example.there.findclips.databinding.FragmentVideosSearchBinding
import com.example.there.findclips.model.entities.Video
import com.example.there.findclips.view.lists.VideosList
import com.example.there.findclips.fragments.search.MainSearchFragment
import com.example.there.findclips.util.app
import com.example.there.findclips.util.basePlayerActivity
import com.example.there.findclips.view.recycler.EndlessRecyclerOnScrollListener
import com.example.there.findclips.view.recycler.SeparatorDecoration
import com.example.there.findclips.util.screenOrientation
import com.example.there.findclips.view.lists.OnVideoClickListener
import javax.inject.Inject


class VideosSearchFragment : BaseVMFragment<VideosSearchViewModel>(), MainSearchFragment {

    override var query: String = ""
        set(value) {
            if (field == value) return
            field = value
            viewModel.searchVideos(value)
            viewModel.viewState.videos.clear()
        }

    @Inject
    lateinit var viewModelFactory: VideosSearchVMFactory

    private val videoItemClickListener = object : OnVideoClickListener {
        override fun onClick(item: Video) {
            basePlayerActivity?.playVideo(video = item)
        }
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
            }
        }
    }

    override fun initComponent() {
        activity?.app?.createVideosSearchComponent()?.inject(this)
    }

    override fun releaseComponent() {
        activity?.app?.releaseVideosSearchComponent()
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(VideosSearchViewModel::class.java)
    }

    companion object {
        private const val ARG_QUERY = "ARG_QUERY"

        fun newInstanceWithQuery(query: String): VideosSearchFragment = VideosSearchFragment().apply {
            arguments = Bundle().apply { putString(ARG_QUERY, query) }
        }
    }
}
