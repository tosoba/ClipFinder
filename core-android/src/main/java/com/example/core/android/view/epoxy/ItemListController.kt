package com.example.core.android.view.epoxy

import android.os.Handler
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.epoxy.VisibilityState
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.MvRxState
import com.example.core.android.*
import com.example.core.android.di.EpoxyHandlerQualifier
import com.example.core.android.model.*
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
                message(requireContext().getString(R.string.error_occurred))
            }
        } else {
            items.value.forEach {
                buildItem(it).spanSizeOverride { _, _, _ -> 1 }.addTo(this)
            }

            if (loadMore != null && items is HoldsPagedData<*, *> && items.shouldLoadMore) {
                loadingIndicator {
                    id("loading-indicator-more-items")
                    onBind { _, _, _ -> loadMore() }
                }
            }
        }
    }
}

inline fun <S : MvRxState, L : DefaultLoadable<IL>, IL : ItemsList<I>, I> BaseMvRxFragment.pagedItemListController(
    prop: KProperty1<S, L>,
    modelBuildingHandler: Handler = get(EpoxyHandlerQualifier.BUILDER),
    diffingHandler: Handler = get(EpoxyHandlerQualifier.DIFFER),
    headerText: String? = null,
    noinline loadMore: (() -> Unit)? = null,
    noinline shouldOverrideBuildModels: (S) -> Boolean = { false },
    noinline overrideBuildModels: (TypedEpoxyController<S>.(S) -> Unit)? = null,
    crossinline reloadClicked: () -> Unit,
    crossinline clearFailure: () -> Unit,
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

        val loadable = prop.get(data)
        val value = loadable.value
        if (value.items.isEmpty()) when (loadable) {
            is DefaultInProgress<*> -> loadingIndicator { id("loading-indicator-items") }
            is DefaultFailed<*> -> reloadControl {
                id("reload-control")
                onReloadClicked { _ -> reloadClicked() }
                message(requireContext().getString(R.string.error_occurred))
            }
        } else {
            value.items.forEach {
                buildItem(it).spanSizeOverride { _, _, _ -> 1 }.addTo(this)
            }

            when (loadable) {
                is DefaultFailed<*> -> ReloadControlBindingModel_()
                    .id("reload-control")
                    .message(requireContext().getString(R.string.error_occurred))
                    .onVisibilityStateChanged { _, _, visibilityState ->
                        if (visibilityState == VisibilityState.INVISIBLE) clearFailure()
                    }
                    .onReloadClicked { _ -> reloadClicked() }
                else -> if (loadMore != null && value is CompletionTrackable && !value.completed) {
                    loadingIndicator {
                        id("loading-indicator-more-items")
                        onBind { _, _, _ -> loadMore() }
                    }
                }
            }
        }
    }
}
