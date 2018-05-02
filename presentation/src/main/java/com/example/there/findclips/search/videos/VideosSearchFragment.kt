package com.example.there.findclips.search.videos

import android.arch.lifecycle.ViewModelProviders
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.base.BaseVMFragment
import com.example.there.findclips.entities.Video
import com.example.there.findclips.lists.VideosList
import com.example.there.findclips.search.MainSearchFragment
import com.example.there.findclips.util.SeparatorDecoration
import com.example.there.findclips.util.app
import com.example.there.findclips.util.screenOrientation
import javax.inject.Inject
import com.example.there.findclips.databinding.FragmentVideosSearchBinding


class VideosSearchFragment : BaseVMFragment<VideosSearchViewModel>(), MainSearchFragment {

    override val title: String
        get() = "Search"

    override var query: String = ""
        set(value) {
            if (field == value) return
            field = value
            mainViewModel.getVideos(value)
            mainViewModel.viewState.videos.clear()
        }

    @Inject
    lateinit var viewModelFactory: VideosSearchVMFactory

    private val videoItemClickListener = object : VideosList.OnItemClickListener {
        override fun onClick(item: Video) {

        }
    }

    private val view: VideosSearchView by lazy {
        VideosSearchView(
                state = mainViewModel.viewState,
                videosAdapter = VideosList.Adapter(mainViewModel.viewState.videos, R.layout.video_item, videoItemClickListener),
                videosLayoutManager = if (context?.screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                    GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                } else {
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                },
                videosItemDecoration = SeparatorDecoration(context!!, context!!.resources.getColor(R.color.colorAccent), 2f)
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentVideosSearchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_videos_search, container, false)
        binding.videosSearchView = view
        return binding.root
    }

    override fun initComponent() {
        activity?.app?.createVideosSearchComponent()?.inject(this)
    }

    override fun releaseComponent() {
        activity?.app?.releaseVideosSearchComponent()
    }

    override fun initViewModel() {
        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(VideosSearchViewModel::class.java)
    }
}
