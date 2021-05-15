package com.clipfinder.core.android.view.epoxy

import android.os.Handler
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.MvRxState
import com.clipfinder.core.android.di.EpoxyHandlerType
import com.clipfinder.core.ext.qualifier
import org.koin.android.ext.android.get

inline fun <S : MvRxState> BaseMvRxFragment.injectedTypedController(
    crossinline build: EpoxyController.(S) -> Unit
): TypedEpoxyController<S> =
    typedController(
        modelBuildingHandler = get(EpoxyHandlerType.BUILDER.qualifier),
        diffingHandler = get(EpoxyHandlerType.DIFFER.qualifier),
        build = build
    )

inline fun <S : MvRxState> BaseMvRxFragment.typedController(
    modelBuildingHandler: Handler,
    diffingHandler: Handler,
    crossinline build: EpoxyController.(S) -> Unit
): TypedEpoxyController<S> =
    object : TypedEpoxyController<S>(modelBuildingHandler, diffingHandler) {
        override fun buildModels(data: S) {
            if (view == null || isRemoving) return
            build(data)
        }
    }
