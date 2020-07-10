package com.example.coreandroid.util

import android.os.Handler
import android.view.ViewGroup
import com.airbnb.epoxy.*
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.withState
import com.example.coreandroid.base.vm.MvRxViewModel

inline fun BaseMvRxFragment.typedController(
    crossinline build: EpoxyController.() -> Unit
) = object : AsyncEpoxyController() {
    override fun buildModels() {
        if (view == null || isRemoving) return
        build()
    }
}

inline fun <S : MvRxState, A : MvRxViewModel<S>> BaseMvRxFragment.typedController(
    viewModel: A,
    crossinline buildModels: EpoxyController.(state: S) -> Unit
) = object : AsyncEpoxyController() {
    override fun buildModels() {
        if (view == null || isRemoving) return
        withState(viewModel) { state ->
            buildModels(state)
        }
    }
}

inline fun <S : MvRxState, A : MvRxViewModel<S>> BaseMvRxFragment.typedController(
    modelBuildingHandler: Handler,
    diffingHandler: Handler,
    viewModel: A,
    crossinline build: EpoxyController.(state: S) -> Unit
) = object : TypedEpoxyController<S>(modelBuildingHandler, diffingHandler) {
    override fun buildModels(data: S) {
        if (view == null || isRemoving) return
        withState(viewModel) { state ->
            build(state)
        }
    }
}

inline fun <A : BaseMvRxViewModel<B>, B : MvRxState, C : BaseMvRxViewModel<D>, D : MvRxState> BaseMvRxFragment.typedController(
    viewModel1: A,
    viewModel2: C,
    crossinline build: EpoxyController.(state1: B, state2: D) -> Unit
) = object : AsyncEpoxyController() {
    override fun buildModels() {
        if (view == null || isRemoving) return
        withState(viewModel1, viewModel2) { state1, state2 ->
            build(state1, state2)
        }
    }
}

open class NestedScrollingCarouselModel : CarouselModel_() {
    override fun buildView(parent: ViewGroup): Carousel = super.buildView(parent).apply {
        isNestedScrollingEnabled = false
    }
}

inline fun EpoxyController.carousel(modelInitializer: CarouselModelBuilder.() -> Unit) {
    NestedScrollingCarouselModel()
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
    extraModels: Collection<EpoxyModel<*>> = emptyList(),
    modelBuilder: (T) -> EpoxyModel<*>
) {
    models(items.map { modelBuilder(it) } + extraModels)
}

inline fun <T, R> CarouselModelBuilder.withModelsFrom(
    items: Map<T, R>,
    modelBuilder: (T, R) -> EpoxyModel<*>
) {
    models(items.map { (key, value) -> modelBuilder(key, value) })
}
