package com.example.core.android.view.epoxy

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.airbnb.epoxy.*
import com.example.core.android.*
import com.example.core.android.model.*

open class NestedScrollingCarouselModel : CarouselModel_() {
    override fun buildView(parent: ViewGroup): Carousel = super.buildView(parent)
        .apply { isNestedScrollingEnabled = false }
}

inline fun EpoxyController.carousel(modelInitializer: CarouselModelBuilder.() -> Unit) {
    NestedScrollingCarouselModel()
        .apply(modelInitializer)
        .addTo(this)
}

inline fun <T> CarouselModelBuilder.withModelsFrom(
    items: Collection<T>,
    modelBuilder: (T) -> EpoxyModel<*>
) {
    models(items.map(modelBuilder))
}

inline fun <T> CarouselModelBuilder.withModelsFrom(
    items: Collection<T>,
    extraModels: Iterable<EpoxyModel<*>> = emptyList(),
    modelBuilder: (T) -> EpoxyModel<*>
) {
    models(items.map(modelBuilder) + extraModels)
}

inline fun <Item> EpoxyController.pagedDataListCarouselWithHeader(
    context: Context,
    data: PagedDataList<Item>,
    @StringRes headerRes: Int,
    idSuffix: String,
    crossinline loadItems: () -> Unit,
    crossinline clearFailure: () -> Unit,
    buildItem: (Item) -> EpoxyModel<*>
) {
    pagedDataListCarouselWithHeader(
        context, data, headerRes, idSuffix, loadItems, clearFailure, { it }, buildItem
    )
}

inline fun <Value, Item, P : HoldsPagedData<Value, P>> EpoxyController.pagedDataListCarouselWithHeader(
    context: Context,
    data: HoldsPagedData<Value, P>,
    @StringRes headerRes: Int,
    idSuffix: String,
    crossinline loadItems: () -> Unit,
    crossinline clearFailure: () -> Unit,
    mapToItems: (Collection<Value>) -> Collection<Item>,
    buildItem: (Item) -> EpoxyModel<*>
) {
    headerItem {
        id("header-$idSuffix")
        text(context.getString(headerRes))
    }

    val value = data.value
    val status = data.status
    if (value.isEmpty()) {
        if (status is Loading) loadingIndicator {
            id("loading-indicator-$idSuffix")
        } else if (status is LoadingFailed<*>) reloadControl {
            id("reload-control-$idSuffix")
            onReloadClicked { _ -> loadItems() }
            message(context.getString(R.string.error_occurred))
        }
    } else carousel {
        id(idSuffix)
        withModelsFrom<Item>(
            items = mapToItems(value),
            extraModels = when {
                status is LoadingFailed<*> -> listOf(
                    ReloadControlBindingModel_()
                        .id("reload-control-$idSuffix")
                        .message(context.getString(R.string.error_occurred))
                        .onVisibilityStateChanged { _, _, visibilityState ->
                            if (visibilityState == VisibilityState.INVISIBLE) clearFailure()
                        }
                        .onReloadClicked { _ -> loadItems() }
                )
                data.shouldLoadMore -> listOf(
                    LoadingIndicatorBindingModel_()
                        .id("loading-more-$idSuffix")
                        .onBind { _, _, _ -> loadItems() }
                )
                else -> emptyList()
            },
            modelBuilder = buildItem
        )
    }
}

inline fun <I> EpoxyController.defaultLoadableCarouselWithHeader(
    context: Context,
    data: DefaultLoadable<Collection<I>>,
    @StringRes headerRes: Int,
    idSuffix: String,
    crossinline loadItems: () -> Unit,
    crossinline clearFailure: () -> Unit,
    buildItem: (I) -> EpoxyModel<*>
) {
    defaultLoadableCarouselWithHeader(context, data, headerRes, idSuffix, loadItems, clearFailure, { it }, buildItem)
}

inline fun <T, I> EpoxyController.defaultLoadableCarouselWithHeader(
    context: Context,
    loadable: DefaultLoadable<Collection<T>>,
    @StringRes headerRes: Int,
    idSuffix: String,
    crossinline loadItems: () -> Unit,
    crossinline clearFailure: () -> Unit,
    mapToItems: (Collection<T>) -> Collection<I>,
    buildItem: (I) -> EpoxyModel<*>
) {
    headerItem {
        id("header-$idSuffix")
        text(context.getString(headerRes))
    }

    val value = loadable.value
    if (value.isEmpty()) {
        if (loadable is DefaultInProgress) loadingIndicator { id("loading-indicator-$idSuffix") }
        else if (loadable is DefaultFailed) reloadControl {
            id("reload-control-$idSuffix")
            onReloadClicked { _ -> loadItems() }
            message(context.getString(R.string.error_occurred))
        }
    } else carousel {
        id(idSuffix)
        withModelsFrom<I>(
            items = mapToItems(value),
            extraModels = when {
                loadable is DefaultFailed -> listOf(
                    ReloadControlBindingModel_()
                        .id("reload-control-$idSuffix")
                        .message(context.getString(R.string.error_occurred))
                        .onVisibilityStateChanged { _, _, visibilityState ->
                            if (visibilityState == VisibilityState.INVISIBLE) clearFailure()
                        }
                        .onReloadClicked { _ -> loadItems() }
                )
                value is CompletionTrackable && !value.completed -> listOf(
                    LoadingIndicatorBindingModel_()
                        .id("loading-more-$idSuffix")
                        .onBind { _, _, _ -> loadItems() }
                )
                else -> emptyList()
            },
            modelBuilder = buildItem
        )
    }
}

inline fun <I> EpoxyController.loadableCarouselWithHeader(
    context: Context,
    data: Loadable<Collection<I>>,
    @StringRes headerRes: Int,
    idSuffix: String,
    crossinline loadItems: () -> Unit,
    crossinline clearFailure: () -> Unit,
    buildItem: (I) -> EpoxyModel<*>
) {
    loadableCarouselWithHeader(context, data, headerRes, idSuffix, loadItems, clearFailure, { it }, buildItem)
}

inline fun <T, I> EpoxyController.loadableCarouselWithHeader(
    context: Context,
    loadable: Loadable<Collection<T>>,
    @StringRes headerRes: Int,
    idSuffix: String,
    crossinline loadItems: () -> Unit,
    crossinline clearFailure: () -> Unit,
    mapToItems: (Collection<T>) -> Collection<I>,
    buildItem: (I) -> EpoxyModel<*>
) {
    headerItem {
        id("header-$idSuffix")
        text(context.getString(headerRes))
    }

    when (loadable) {
        is LoadingFirst -> loadingIndicator { id("loading-indicator-$idSuffix") }
        is FailedFirst -> reloadControl {
            id("reload-control-$idSuffix")
            onReloadClicked { _ -> loadItems() }
            message(context.getString(R.string.error_occurred))
        }
        is WithValue -> {
            val collection = loadable.value
            carousel {
                id(idSuffix)
                withModelsFrom<I>(
                    items = mapToItems(collection),
                    extraModels = when {
                        loadable is FailedNext -> listOf(
                            ReloadControlBindingModel_()
                                .id("reload-control-$idSuffix")
                                .message(context.getString(R.string.error_occurred))
                                .onVisibilityStateChanged { _, _, visibilityState ->
                                    if (visibilityState == VisibilityState.INVISIBLE) clearFailure()
                                }
                                .onReloadClicked { _ -> loadItems() }
                        )
                        collection is CompletionTrackable && !collection.completed -> listOf(
                            LoadingIndicatorBindingModel_()
                                .id("loading-more-$idSuffix")
                                .onBind { _, _, _ -> loadItems() }
                        )
                        else -> emptyList()
                    },
                    modelBuilder = buildItem
                )
            }
        }
    }
}
