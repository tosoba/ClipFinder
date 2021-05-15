package com.clipfinder.core.android.view.epoxy

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.airbnb.epoxy.*
import com.clipfinder.core.android.*
import com.clipfinder.core.model.*

open class NestedScrollingCarouselModel : CarouselModel_() {
    override fun buildView(parent: ViewGroup): Carousel =
        super.buildView(parent).apply { isNestedScrollingEnabled = false }
}

inline fun EpoxyController.carousel(modelInitializer: CarouselModelBuilder.() -> Unit) {
    NestedScrollingCarouselModel().apply(modelInitializer).addTo(this)
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

inline fun <I> EpoxyController.loadableCarouselWithHeader(
    context: Context,
    data: Loadable<Collection<I>>,
    @StringRes headerRes: Int,
    idSuffix: String,
    crossinline loadItems: () -> Unit,
    crossinline clearFailure: () -> Unit,
    buildItem: (I) -> EpoxyModel<*>
) {
    loadableCarouselWithHeader(
        context,
        data,
        headerRes,
        idSuffix,
        loadItems,
        clearFailure,
        { it },
        buildItem
    )
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
        is FailedFirst ->
            reloadControl {
                id("reload-control-$idSuffix")
                onReloadClicked { _ -> loadItems() }
                message(context.getString(R.string.error_occurred))
            }
        is WithValue -> {
            val collection = loadable.value
            carousel {
                id(idSuffix)
                withModelsFrom(
                    items = mapToItems(collection),
                    extraModels =
                        when {
                            loadable is FailedNext ->
                                listOf(
                                    ReloadControlBindingModel_()
                                        .id("reload-control-$idSuffix")
                                        .message(context.getString(R.string.error_occurred))
                                        .onVisibilityStateChanged { _, _, visibilityState ->
                                            if (visibilityState == VisibilityState.INVISIBLE)
                                                clearFailure()
                                        }
                                        .onReloadClicked { _ -> loadItems() }
                                )
                            collection is CompletionTrackable && !collection.completed ->
                                listOf(
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
