package com.example.youtuberelatedvideos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coreandroid.BR
import com.example.coreandroid.base.fragment.BaseVMFragment
import com.example.coreandroid.base.fragment.IRelatedVideosSearchFragment
import com.example.coreandroid.model.videos.Video
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
import com.example.youtuberelatedvideos.databinding.FragmentRelatedVideosBinding


class RelatedVideosFragment : BaseVMFragment<RelatedVideosViewModel>(RelatedVideosViewModel::class),
        IRelatedVideosSearchFragment {

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
        EndlessRecyclerOnScrollListener(minItemsBeforeLoadingMore = 1) { viewModel.searchRelatedVideosWithToLastId() }
    }

    private val relatedVideosItemDecoration: RecyclerView.ItemDecoration by lazy(LazyThreadSafetyMode.NONE) {
        SeparatorDecoration(context!!, ResourcesCompat.getColor(resources, R.color.colorAccent, null), 2f)
    }

    private val view: RelatedVideosView by lazy {
        RelatedVideosView(viewModel.viewState, relatedVideosRecyclerViewItemView)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = DataBindingUtil.inflate<FragmentRelatedVideosBinding>(
            inflater, R.layout.fragment_related_videos, container, false
    ).apply {
        fragmentView = this@RelatedVideosFragment.view
        relatedVideosRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }.root

    override fun searchRelatedVideos(video: Video) {
        viewModel.searchRelatedVideos(video)
    }
}
