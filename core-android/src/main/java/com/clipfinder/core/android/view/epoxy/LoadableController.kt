package com.clipfinder.core.android.view.epoxy

import android.content.Context
import android.os.Handler
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.epoxy.VisibilityState
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.MvRxState
import com.clipfinder.core.android.*
import com.clipfinder.core.android.di.EpoxyHandlerType
import com.clipfinder.core.ext.qualifier
import com.clipfinder.core.model.*
import org.koin.android.ext.android.get
import kotlin.reflect.KProperty1

inline fun <S : MvRxState, I> BaseMvRxFragment.loadableCollectionController(
    prop: KProperty1<S, Loadable<Collection<I>>>,
    modelBuildingHandler: Handler = get(EpoxyHandlerType.BUILDER.qualifier),
    diffingHandler: Handler = get(EpoxyHandlerType.DIFFER.qualifier),
    headerText: String? = null,
    idSuffix: String = "",
    noinline loadMore: (() -> Unit)? = null,
    noinline shouldOverrideBuildModels: (S) -> Boolean = { false },
    noinline overrideBuildModels: (TypedEpoxyController<S>.(S) -> Unit)? = null,
    crossinline reloadClicked: () -> Unit,
    crossinline clearFailure: () -> Unit,
    crossinline buildItem: (I) -> EpoxyModel<*>
): TypedEpoxyController<S> =
    object : TypedEpoxyController<S>(modelBuildingHandler, diffingHandler) {
        // TODO: consistent and nice looking spacing between items
        override fun buildModels(data: S) {
            if (view == null || isRemoving) return

            if (shouldOverrideBuildModels(data) && overrideBuildModels != null) {
                overrideBuildModels(data)
                return
            }

            loadableCollection(
                requireContext(),
                prop.get(data),
                headerText,
                idSuffix,
                loadMore,
                reloadClicked,
                clearFailure,
                buildItem
            )
        }
    }

inline fun <I> EpoxyController.loadableCollection(
    context: Context,
    loadable: Loadable<Collection<I>>,
    headerText: String? = null,
    idSuffix: String = "",
    noinline loadMore: (() -> Unit)? = null,
    crossinline reloadClicked: () -> Unit,
    crossinline clearFailure: () -> Unit,
    crossinline buildItem: (I) -> EpoxyModel<*>
) {
    if (headerText != null) {
        headerItem {
            id("items-header-$idSuffix")
            text(headerText)
            spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
        }
    }

    when (loadable) {
        is LoadingFirst -> loadingIndicator { id("loading-indicator-items-$idSuffix") }
        is FailedFirst ->
            reloadControl {
                id("reload-control-$idSuffix")
                onReloadClicked { _ -> reloadClicked() }
                message(context.getString(R.string.error_occurred))
            }
        is WithValue -> {
            val value = loadable.value
            value.forEach { buildItem(it).spanSizeOverride { _, _, _ -> 1 }.addTo(this) }

            if (loadable is FailedNext) {
                ReloadControlBindingModel_()
                    .id("reload-control-$idSuffix")
                    .message(context.getString(R.string.error_occurred))
                    .onVisibilityStateChanged { _, _, visibilityState ->
                        if (visibilityState == VisibilityState.INVISIBLE) clearFailure()
                    }
                    .onReloadClicked { _ -> reloadClicked() }
            } else if (loadMore != null && value is CompletionTrackable && !value.completed) {
                loadingIndicator {
                    id("loading-indicator-more-items-$idSuffix")
                    onBind { _, _, _ -> loadMore() }
                }
            }
        }
    }
}
