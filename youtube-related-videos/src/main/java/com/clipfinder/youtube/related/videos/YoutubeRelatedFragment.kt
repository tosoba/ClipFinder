package com.clipfinder.youtube.related.videos

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.clipfinder.core.android.base.fragment.ISearchFragment
import com.clipfinder.core.android.base.handler.YoutubePlayerController
import com.clipfinder.core.android.model.videos.clickableListItem
import com.clipfinder.core.android.util.ext.screenOrientation
import com.clipfinder.core.android.view.epoxy.loadableCollectionController
import com.clipfinder.core.ext.castAs
import kotlinx.android.synthetic.main.fragment_youtube_related.*
import kotlinx.android.synthetic.main.fragment_youtube_related.view.*

class YoutubeRelatedFragment : BaseMvRxFragment(), ISearchFragment {
    private val viewModel: YoutubeRelatedViewModel by fragmentViewModel()

    private val epoxyController: TypedEpoxyController<YoutubeRelatedState> by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        loadableCollectionController(
            YoutubeRelatedState::videos,
            headerText = getString(R.string.videos),
            loadMore = viewModel::search,
            reloadClicked = viewModel::search,
            clearFailure = viewModel::clearVideosError
        ) { video ->
            video.clickableListItem {
                activity?.castAs<YoutubePlayerController>()?.loadVideo(video)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_youtube_related, container, false).apply {
            this.youtube_related_recycler_view.setController(epoxyController)
            this.youtube_related_recycler_view.layoutManager =
                layoutManagerFor(requireContext().screenOrientation)
        }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        youtube_related_recycler_view?.layoutManager = layoutManagerFor(newConfig.orientation)
    }

    override fun invalidate() = withState(viewModel, epoxyController::setData)

    private fun layoutManagerFor(orientation: Int): RecyclerView.LayoutManager =
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        } else {
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }

    override fun search(input: String) = viewModel.search(newVideoId = input)
}
