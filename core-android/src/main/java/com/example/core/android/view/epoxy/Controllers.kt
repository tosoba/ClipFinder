package com.example.core.android.view.epoxy

import android.os.Handler
import com.airbnb.epoxy.AsyncEpoxyController
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.MvRxState
import com.example.core.android.di.EpoxyHandlerQualifier
import org.koin.android.ext.android.inject

inline fun BaseMvRxFragment.asyncController(
    crossinline build: EpoxyController.() -> Unit
) = object : AsyncEpoxyController() {
    override fun buildModels() {
        if (view == null || isRemoving) return
        build()
    }
}

inline fun <S : MvRxState> BaseMvRxFragment.injectedTypedController(
    crossinline build: EpoxyController.(S) -> Unit
): TypedEpoxyController<S> {
    val builder by inject<Handler>(EpoxyHandlerQualifier.BUILDER)
    val differ by inject<Handler>(EpoxyHandlerQualifier.DIFFER)
    return typedController(builder, differ, build)
}

inline fun <S : MvRxState> BaseMvRxFragment.typedController(
    modelBuildingHandler: Handler,
    diffingHandler: Handler,
    crossinline build: EpoxyController.(S) -> Unit
): TypedEpoxyController<S> = object : TypedEpoxyController<S>(modelBuildingHandler, diffingHandler) {
    override fun buildModels(data: S) {
        if (view == null || isRemoving) return
        build(data)
    }
}
