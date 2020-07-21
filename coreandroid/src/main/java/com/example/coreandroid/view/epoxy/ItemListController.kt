package com.example.coreandroid.view.epoxy

import android.os.Handler
import android.view.View
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
import com.example.coreandroid.model.PagedDataList
import com.example.coreandroid.reloadControl
import kotlin.reflect.KProperty1

fun <S : MvRxState, A : MvRxViewModel<S>, L : HoldsData<Collection<I>>, I> BaseMvRxFragment.itemListController(
    modelBuildingHandler: Handler,
    diffingHandler: Handler,
    viewModel: A,
    prop: KProperty1<S, L>,
    headerText: String? = null,
    loadMore: (() -> Unit)? = null,
    reloadClicked: () -> Unit,
    buildItem: (I) -> EpoxyModel<*>
) = object : TypedEpoxyController<S>(modelBuildingHandler, diffingHandler) {

    //TODO: consistent and nice looking spacing between items
    override fun buildModels(data: S) {
        if (view == null || isRemoving) return

        withState(viewModel) { state ->
            if (headerText != null) headerItem {
                id("items-header")
                text(headerText)
                spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
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
                items.value.forEach {
                    buildItem(it).spanSizeOverride { _, _, _ -> 1 }.addTo(this)
                }

                if (items.status is Loading && items is PagedDataList<*> && items.canLoadMore && loadMore != null) {
                    loadingIndicator {
                        id("loading-indicator-more-items") //TODO: maybe use a different indicator for loading more
                        onBind { _, _, _ -> loadMore() }
                    }
                }
            }
        }
    }
}
