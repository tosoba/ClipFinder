package com.example.there.findclips.fragments.relatedvideos

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.base.BaseVMFragment
import com.example.there.findclips.databinding.FragmentRelatedVideosBinding
import com.example.there.findclips.model.entities.Video
import com.example.there.findclips.view.lists.RelatedVideosList
import com.example.there.findclips.util.app
import com.example.there.findclips.util.onVideoSelectedListener
import com.example.there.findclips.view.lists.OnVideoClickListener
import com.example.there.findclips.view.recycler.EndlessRecyclerOnScrollListener
import com.example.there.findclips.view.recycler.SeparatorDecoration
import javax.inject.Inject


class RelatedVideosFragment : BaseVMFragment<RelatedVideosViewModel>() {

    var videoId: String? = null
        set(value) {
            if (value == null) return
            field = value
            viewModel.searchRelatedVideos(value)
        }

    private val onScrollListener = object : EndlessRecyclerOnScrollListener() {
        override fun onLoadMore() {
            viewModel.searchRelatedVideosWithToLastId()
        }
    }

    private val onVideoItemClickListener = object : OnVideoClickListener {
        override fun onClick(item: Video) {
            onVideoSelectedListener?.onVideoSelected(video = item)
        }
    }

    private val view: RelatedVideosView by lazy {
        RelatedVideosView(
                state = viewModel.viewState,
                videosAdapter = RelatedVideosList.Adapter(viewModel.viewState.videos, R.layout.related_video_item, onVideoItemClickListener),
                onScrollListener = onScrollListener,
                videosItemDecoration = SeparatorDecoration(context!!, ResourcesCompat.getColor(resources, R.color.colorAccent, null), 2f)
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentRelatedVideosBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_related_videos, container, false)
        binding.view = view
        binding.relatedVideosRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return binding.root
    }

    override fun initComponent() {
        activity?.app?.createRelatedVideosSubComponent()?.inject(this)
    }

    override fun releaseComponent() {
        activity?.app?.releaseRelatedVideosSubComponent()
    }

    @Inject
    lateinit var factory: RelatedVideosVMFactory

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(RelatedVideosViewModel::class.java)
    }
}
