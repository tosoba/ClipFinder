package com.example.coreandroid.util

import android.os.Handler
import android.view.ViewGroup
import com.airbnb.epoxy.*
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.withState
import com.example.coreandroid.base.vm.MvRxViewModel
import com.example.coreandroid.view.recyclerview.listener.EndlessRecyclerOnScrollListener


fun BaseMvRxFragment.simpleController(
        build: EpoxyController.() -> Unit
) = object : AsyncEpoxyController() {
    override fun buildModels() {
        if (view == null || isRemoving) return
        build()
    }
}

fun <S : MvRxState, A : MvRxViewModel<S>> BaseMvRxFragment.simpleController(
        viewModel: A, buildModels: EpoxyController.(state: S) -> Unit
) = object : AsyncEpoxyController() {
    override fun buildModels() {
        if (view == null || isRemoving) return
        withState(viewModel) { state ->
            buildModels(state)
        }
    }
}

fun <S : MvRxState, A : MvRxViewModel<S>> BaseMvRxFragment.asyncController(
        modelBuildingHandler: Handler, diffingHandler: Handler,
        viewModel: A, build: EpoxyController.(state: S) -> Unit
) = object : TypedEpoxyController<S>(modelBuildingHandler, diffingHandler) {
    override fun buildModels(data: S) {
        if (view == null || isRemoving) return
        withState(viewModel) { state ->
            build(state)
        }
    }
}

fun <A : BaseMvRxViewModel<B>, B : MvRxState, C : BaseMvRxViewModel<D>, D : MvRxState> BaseMvRxFragment.simpleController(
        viewModel1: A, viewModel2: C,
        buildUsing: EpoxyController.(state1: B, state2: D) -> Unit
) = object : AsyncEpoxyController() {
    override fun buildModels() {
        if (view == null || isRemoving) return
        withState(viewModel1, viewModel2) { state1, state2 ->
            buildUsing(state1, state2)
        }
    }
}

open class NestedScrollingCarouselModel : CarouselModel_() {
    override fun buildView(parent: ViewGroup): Carousel = super.buildView(parent).apply {
        isNestedScrollingEnabled = false
    }
}

class InfiniteNestedScrollingCarouselModel(
        private val visibleThreshold: Int = 5,
        private val minItemsBeforeLoadingMore: Int = 0,
        private val onLoadMore: () -> Unit
) : NestedScrollingCarouselModel() {

    override fun buildView(parent: ViewGroup): Carousel = super.buildView(parent).apply {
        addOnScrollListener(EndlessRecyclerOnScrollListener(visibleThreshold, minItemsBeforeLoadingMore) {
            this@InfiniteNestedScrollingCarouselModel.onLoadMore()
        })
    }
}

inline fun EpoxyController.carousel(modelInitializer: CarouselModelBuilder.() -> Unit) {
    NestedScrollingCarouselModel()
            .apply(modelInitializer)
            .addTo(this)
}

inline fun EpoxyController.infiniteCarousel(
        visibleThreshold: Int = 5,
        minItemsBeforeLoadingMore: Int = 0,
        noinline onLoadMore: () -> Unit,
        modelInitializer: CarouselModelBuilder.() -> Unit) {
    InfiniteNestedScrollingCarouselModel(visibleThreshold, minItemsBeforeLoadingMore, onLoadMore)
            .apply(modelInitializer)
            .addTo(this)
}

inline fun <T> CarouselModelBuilder.withModelsFrom(
        items: Collection<T>, modelBuilder: (T) -> EpoxyModel<*>
) {
    models(items.map { modelBuilder(it) })
}

inline fun <T> CarouselModelBuilder.withModelsFrom(
        items: Collection<T>,
        extraModels: Collection<EpoxyModel<*>>,
        modelBuilder: (T) -> EpoxyModel<*>
) {
    models(items.map { modelBuilder(it) } + extraModels)
}

inline fun <T, R> CarouselModelBuilder.withModelsFrom(
        items: Map<T, R>, modelBuilder: (T, R) -> EpoxyModel<*>
) {
    models(items.map { (key, value) -> modelBuilder(key, value) })
}