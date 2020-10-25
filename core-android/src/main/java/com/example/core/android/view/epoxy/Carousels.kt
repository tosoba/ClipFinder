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

inline fun <Item> EpoxyController.dataListCarouselWithHeader(
    context: Context,
    data: DataList<Item>,
    @StringRes headerRes: Int,
    idSuffix: String,
    crossinline loadItems: () -> Unit,
    buildItem: (Item) -> EpoxyModel<*>
) {
    dataListCarouselWithHeader(
        context, data, headerRes, idSuffix, loadItems, { it }, buildItem
    )
}

inline fun <Value, Item> EpoxyController.dataListCarouselWithHeader(
    context: Context,
    data: DataList<Value>,
    @StringRes headerRes: Int,
    idSuffix: String,
    crossinline loadItems: () -> Unit,
    mapToItems: (Collection<Value>) -> Collection<Item>,
    buildItem: (Item) -> EpoxyModel<*>
) {
    headerItem {
        id("header-$idSuffix")
        text(context.getString(headerRes))
    }

    val (value, status) = data
    when (status) {
        is Loading -> loadingIndicator {
            id("loading-indicator-$idSuffix")
        }

        is LoadingFailed<*> -> reloadControl {
            id("reload-control-$idSuffix")
            onReloadClicked { _ -> loadItems() }
            message(context.getString(R.string.error_occurred))
        }

        is LoadedSuccessfully -> carousel {
            id(idSuffix)
            withModelsFrom(mapToItems(value), buildItem)
        }
    }
}

inline fun <Item> EpoxyController.pagedDataListCarouselWithHeader(
    context: Context,
    data: PagedDataList<Item>,
    @StringRes headerRes: Int,
    idSuffix: String,
    crossinline loadItems: () -> Unit,
    buildItem: (Item) -> EpoxyModel<*>
) {
    pagedDataListCarouselWithHeader(
        context, data, headerRes, idSuffix, loadItems, { it }, buildItem
    )
}

inline fun <Value, Item> EpoxyController.pagedDataListCarouselWithHeader(
    context: Context,
    data: HoldsPagedData<Value>,
    @StringRes headerRes: Int,
    idSuffix: String,
    crossinline loadItems: () -> Unit,
    mapToItems: (Collection<Value>) -> Collection<Item>,
    buildItem: (Item) -> EpoxyModel<*>
) {
    headerItem {
        id("header-$idSuffix")
        text(context.getString(headerRes))
    }

    val value = data.value
    val status = data.status
    if (value.isEmpty()) when (status) {
        is Loading -> loadingIndicator {
            id("loading-indicator-$idSuffix")
        }
        is LoadingFailed<*> -> reloadControl {
            id("reload-control-$idSuffix")
            onReloadClicked { _ -> loadItems() }
            message(context.getString(R.string.error_occurred))
        }
    } else carousel {
        id(idSuffix)
        withModelsFrom(
            items = mapToItems(value),
            extraModels = when (status) {
                is LoadingFailed<*> -> listOf(
                    ReloadControlBindingModel_()
                        .id("reload-control-$idSuffix")
                        .message(context.getString(R.string.error_occurred))
                        .onReloadClicked { _ -> loadItems() }
                )
                else -> if (data.shouldLoadMore) listOf(
                    LoadingIndicatorBindingModel_()
                        .id("loading-more-$idSuffix")
                        .onBind { _, _, _ -> loadItems() }
                ) else emptyList()
            },
            modelBuilder = buildItem
        )
    }
}
