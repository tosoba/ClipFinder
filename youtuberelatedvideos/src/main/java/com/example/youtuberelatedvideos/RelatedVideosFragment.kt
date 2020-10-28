package com.example.youtuberelatedvideos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.core.android.BR
import com.example.core.android.base.fragment.BaseVMFragment
import com.example.core.android.base.fragment.IRelatedVideosSearchFragment
import com.example.core.android.base.handler.YoutubePlayerController
import com.example.core.android.model.videos.Video
import com.example.core.android.view.recyclerview.binder.ItemBinder
import com.example.core.android.view.recyclerview.binder.ItemBinderBase
import com.example.core.android.view.recyclerview.decoration.SeparatorDecoration
import com.example.core.android.view.recyclerview.item.ListItemView
import com.example.core.android.view.recyclerview.item.RecyclerViewItemView
import com.example.core.android.view.recyclerview.item.RecyclerViewItemViewState
import com.example.core.android.view.recyclerview.listener.ClickHandler
import com.example.core.android.view.recyclerview.listener.EndlessRecyclerOnScrollListener
import com.example.core.ext.castAs
import com.example.youtuberelatedvideos.databinding.FragmentRelatedVideosBinding

class RelatedVideosFragment :
    BaseVMFragment<RelatedVideosViewModel>(RelatedVideosViewModel::class),
    IRelatedVideosSearchFragment {

    private val relatedVideosRecyclerViewItemView: RecyclerViewItemView<Video> by lazy(LazyThreadSafetyMode.NONE) {
        RecyclerViewItemView(
            RecyclerViewItemViewState(
                viewModel.viewState.initialVideosLoadingInProgress,
                viewModel.viewState.videos,
                viewModel.viewState.videosLoadingErrorOccurred
            ),
            object : ListItemView<Video>(viewModel.viewState.videos) {
                override val itemViewBinder: ItemBinder<Video>
                    get() = ItemBinderBase(BR.video, R.layout.video_item)
            },
            ClickHandler { activity?.castAs<YoutubePlayerController>()?.loadVideo(it) },
            relatedVideosItemDecoration,
            onRelatedVideosScrollListener
        )
    }

    private val onRelatedVideosScrollListener: RecyclerView.OnScrollListener by lazy(LazyThreadSafetyMode.NONE) {
        EndlessRecyclerOnScrollListener(minItemsBeforeLoadingMore = 1) {
            viewModel.searchRelatedVideosWithToLastId()
        }
    }

    private val relatedVideosItemDecoration: RecyclerView.ItemDecoration by lazy(LazyThreadSafetyMode.NONE) {
        SeparatorDecoration(
            requireContext(),
            ResourcesCompat.getColor(resources, R.color.colorAccent, null),
            2f
        )
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
