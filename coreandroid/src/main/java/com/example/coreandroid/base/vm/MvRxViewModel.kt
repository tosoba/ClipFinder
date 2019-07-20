package com.example.coreandroid.base.vm

import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.MvRxState
import com.example.coreandroid.model.Data
import com.example.coreandroid.model.LoadedSuccessfully
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import kotlin.reflect.KProperty

open class MvRxViewModel<S : MvRxState>(
        initialState: S, debugMode: Boolean = false
) : BaseMvRxViewModel<S>(initialState, debugMode) {

    fun <T> Single<T>.update(
            getter: KProperty<Data<T>>,
            onError: (Throwable) -> Unit = {},
            stateReducer: S.(Data<T>) -> S
    ): Disposable {
        setState { stateReducer(getter.call().copyWithLoadingInProgress) }
        return map { Data(it, LoadedSuccessfully) }
                .doOnError { setState { stateReducer(getter.call().copyWithError(it)) } }
                .subscribe({ data -> setState { stateReducer(data) } }, onError)
                .disposeOnClear()
    }
}