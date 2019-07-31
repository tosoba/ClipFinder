package com.example.coreandroid.view.epoxy

import android.content.res.Configuration
import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.withState
import com.example.coreandroid.base.vm.MvRxViewModel
import com.example.coreandroid.headerItem
import com.example.coreandroid.loadingIndicator
import com.example.coreandroid.model.HoldsData
import com.example.coreandroid.model.Loading
import com.example.coreandroid.model.LoadingFailed
import com.example.coreandroid.reloadControl
import kotlin.reflect.KProperty1


fun <S : MvRxState, A : MvRxViewModel<S>, L : HoldsData<Collection<I>>, I> BaseMvRxFragment.itemListController(
        modelBuildingHandler: Handler, diffingHandler: Handler,
        viewModel: A, prop: KProperty1<S, L>, headerText: String? = null,
        columnsWhenVertical: Int = 3, columnsWhenHorizontal: Int = 4,
        onScrollListener: RecyclerView.OnScrollListener? = null,
        reloadClicked: () -> Unit, buildItem: (I) -> EpoxyModel<*>
) = object : TypedEpoxyController<S>(modelBuildingHandler, diffingHandler) {

    //TODO: add a possibility to change span count depending on orientation

    override fun buildModels(data: S) {
        if (view == null || isRemoving) return

        // no idea if this will work - maybe just make sure to invalidate controller onConfigurationChanged...
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (columnsWhenHorizontal > 1) spanCount = columnsWhenHorizontal
        } else {
            if (columnsWhenVertical > 1) spanCount = columnsWhenVertical
        }

        withState(viewModel) { state ->
            if (headerText != null) headerItem {
                id("items-header")
                text(headerText)
            }

            val items = prop.get(state)
            if (items.value.isEmpty()) when (items.status) {
                is Loading -> loadingIndicator {
                    id("loading-indicator-items")
                }

                is LoadingFailed<*> -> reloadControl {
                    id("reload-control")
                    onReloadClicked(View.OnClickListener { reloadClicked() })
                    message("Error occurred lmao") //TODO: error msg
                }
            } else {
                items.value.forEach { buildItem(it).addTo(this) }

                if (items.status is Loading) {
                    loadingIndicator {
                        id("loading-indicator-more-items") //TODO: maybe use a different indicator for loading more
                    }
                }
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        onScrollListener?.let { recyclerView.addOnScrollListener(it) }
    }
}