package com.example.core.android.view.epoxy

import android.os.Handler
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.MvRxState
import com.example.core.android.di.EpoxyHandlerQualifier
import com.example.core.android.headerItem
import com.example.core.android.loadingIndicator
import com.example.core.android.model.HoldsData
import com.example.core.android.model.HoldsPagedData
import com.example.core.android.model.Loading
import com.example.core.android.model.LoadingFailed
import com.example.core.android.reloadControl
import org.koin.android.ext.android.get
import kotlin.reflect.KProperty1

inline fun <S : MvRxState, L : HoldsData<Collection<I>>, I> BaseMvRxFragment.itemListController(
    prop: KProperty1<S, L>,
    modelBuildingHandler: Handler = get(EpoxyHandlerQualifier.BUILDER),
    diffingHandler: Handler = get(EpoxyHandlerQualifier.DIFFER),
    headerText: String? = null,
    noinline loadMore: (() -> Unit)? = null,
    noinline shouldOverrideBuildModels: (S) -> Boolean = { false },
    noinline overrideBuildModels: (TypedEpoxyController<S>.(S) -> Unit)? = null,
    crossinline reloadClicked: () -> Unit,
    crossinline buildItem: (I) -> EpoxyModel<*>
): TypedEpoxyController<S> = object : TypedEpoxyController<S>(modelBuildingHandler, diffingHandler) {

    //TODO: consistent and nice looking spacing between items
    override fun buildModels(data: S) {
        if (view == null || isRemoving) return

        if (shouldOverrideBuildModels(data) && overrideBuildModels != null) {
            overrideBuildModels(data)
            return
        }

        if (headerText != null) headerItem {
            id("items-header")
            text(headerText)
            spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
        }

        val items = prop.get(data)
        if (items.value.isEmpty()) when (items.status) {
            is Loading -> loadingIndicator {
                id("loading-indicator-items")
            }

            is LoadingFailed<*> -> reloadControl {
                id("reload-control")
                onReloadClicked { _ -> reloadClicked() }
                message("Error occurred lmao") //TODO: error msg
            }
        } else {
            items.value.forEach {
                buildItem(it).spanSizeOverride { _, _, _ -> 1 }.addTo(this)
            }

            if (loadMore != null && items is HoldsPagedData<*, *> && items.shouldLoadMore) {
                loadingIndicator {
                    id("loading-indicator-more-items") //TODO: maybe use a different indicator for loading more
                    onBind { _, _, _ -> loadMore() }
                }
            }
        }
    }
}
