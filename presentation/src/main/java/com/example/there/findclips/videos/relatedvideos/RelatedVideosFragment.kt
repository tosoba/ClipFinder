package com.example.there.findclips.videos.relatedvideos

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.view.recycler.SeparatorDecoration

class RelatedVideosFragment : com.example.coreandroid.base.fragment.BaseVMFragment<RelatedVideosViewModel>(RelatedVideosViewModel::class.java), com.example.coreandroid.di.Injectable {

    private val relatedVideosRecyclerViewItemView: RecyclerViewItemView<VideoItemView> by lazy(LazyThreadSafetyMode.NONE) {
        RecyclerViewItemView(
                RecyclerViewItemViewState(viewModel.viewState.initialVideosLoadingInProgress, viewModel.viewState.videos, viewModel.viewState.videosLoadingErrorOccurred),
                object : ListItemView<VideoItemView>(viewModel.viewState.videos) {
                    override val itemViewBinder: ItemBinder<VideoItemView>
                        get() = ItemBinderBase(BR.videoView, R.layout.video_item)
                },
                ClickHandler { youtubePlayerController?.loadVideo(it.video) },
                relatedVideosItemDecoration,
                onRelatedVideosScrollListener
        )
    }

    private val onRelatedVideosScrollListener: RecyclerView.OnScrollListener by lazy(LazyThreadSafetyMode.NONE) {
        object : com.example.coreandroid.view.list.listener.EndlessRecyclerOnScrollListener(returnFromOnScrolledItemCount = 1) {
            override fun onLoadMore() = viewModel.searchRelatedVideosWithToLastId()
        }
    }

    private val relatedVideosItemDecoration: RecyclerView.ItemDecoration by lazy(LazyThreadSafetyMode.NONE) {
        SeparatorDecoration(context!!, ResourcesCompat.getColor(resources, R.color.colorAccent, null), 2f)
    }

    private val view: RelatedVideosView by lazy {
        RelatedVideosView(viewModel.viewState, relatedVideosRecyclerViewItemView)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = DataBindingUtil.inflate<com.example.there.findclips.databinding.FragmentRelatedVideosBinding>(
            inflater, R.layout.fragment_related_videos, container, false
    ).apply {
        fragmentView = this@RelatedVideosFragment.view
        relatedVideosRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }.root

    fun searchRelatedVideos(video: Video) {
        viewModel.searchRelatedVideos(video)
    }
}
