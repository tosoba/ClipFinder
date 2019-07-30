package com.example.coreandroid.view.epoxy

import android.content.res.Configuration
import android.os.Handler
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.withState
import com.example.coreandroid.base.vm.MvRxViewModel
import com.example.coreandroid.model.HoldsData
import com.example.coreandroid.model.spotify.Playlist
import com.example.coreandroid.view.recyclerview.listener.EndlessRecyclerOnScrollListener
import kotlin.reflect.KProperty1


fun <S : MvRxState, A : MvRxViewModel<S>, L : HoldsData<Collection<Playlist>>> BaseMvRxFragment.playlistListController(
        modelBuildingHandler: Handler, diffingHandler: Handler,
        viewModel: A, prop: KProperty1<S, L>, headerText: String? = "Playlists",
        columnsWhenVertical: Int = 3, columnsWhenHorizontal: Int = 4,
        visibleThreshold: Int = 5, minItemsBeforeLoadingMore: Int = 0, onLoadMore: (() -> Unit)? = null
) = object : TypedEpoxyController<L>(modelBuildingHandler, diffingHandler) {

    //TODO: add argument for header
    //TODO: add a possibility to change span count depending on orientation


    override fun buildModels(data: L) {
        if (view == null || isRemoving) return

        // no idea if this will work - maybe just make sure to invalidate controller onConfigurationChanged...
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (columnsWhenHorizontal > 1) spanCount = columnsWhenHorizontal
        } else {
            if (columnsWhenVertical > 1) spanCount = columnsWhenVertical
        }

        withState(viewModel) { state ->
            val playlists = prop.get(state)

        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        onLoadMore?.let {
            recyclerView.addOnScrollListener(object : EndlessRecyclerOnScrollListener(visibleThreshold, minItemsBeforeLoadingMore) {
                override fun onLoadMore() = it.invoke()
            })
        }
    }
}