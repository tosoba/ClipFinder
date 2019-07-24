package com.example.coreandroid.base.vm

import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.MvRxState
import com.example.coreandroid.model.HoldsData
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import kotlin.reflect.KProperty

open class MvRxViewModel<S : MvRxState>(
        initialState: S, debugMode: Boolean = false
) : BaseMvRxViewModel<S>(initialState, debugMode) {

    @Suppress("UNCHECKED_CAST")
    protected fun <T, D : HoldsData<T>> Single<T>.update(
            getter: KProperty<D>,
            onError: (Throwable) -> Unit = {},
            stateReducer: S.(D) -> S
    ): Disposable {
        setState { stateReducer(getter.call().copyWithLoadingInProgress as D) }
        return map { getter.call().success(it) }
                .doOnError { setState { stateReducer(getter.call().copyWithError(it) as D) } }
                .subscribe({ data -> setState { stateReducer(data as D) } }, onError)
                .disposeOnClear()
    }
}
