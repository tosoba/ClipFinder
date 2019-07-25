package com.example.coreandroid.base.vm

import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.MvRxState
import com.example.core.model.Resource
import com.example.coreandroid.model.HoldsData
import io.reactivex.Observable
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

    @Suppress("UNCHECKED_CAST")
    protected fun <T, D : HoldsData<T>> Single<Resource<T>>.updateWithResource(
            getter: KProperty<D>,
            onError: (Throwable) -> Unit = {},
            stateReducer: S.(D) -> S
    ): Disposable {
        setState { stateReducer(getter.call().copyWithLoadingInProgress as D) }
        return map {
            when (it) {
                is Resource.Success -> getter.call().success(it.data)
                is Resource.Error<T, *> -> getter.call().copyWithError(it.error)
            }
        }.doOnError { setState { stateReducer(getter.call().copyWithError(it) as D) } }
                .subscribe({ data -> setState { stateReducer(data as D) } }, onError)
                .disposeOnClear()
    }

    @Suppress("UNCHECKED_CAST")
    protected fun <T, D : HoldsData<T>> Observable<Resource<T>>.updateWithResource(
            getter: KProperty<D>,
            onError: (Throwable) -> Unit = {},
            stateReducer: S.(D) -> S
    ): Disposable {
        setState { stateReducer(getter.call().copyWithLoadingInProgress as D) }
        return map {
            when (it) {
                is Resource.Success -> getter.call().success(it.data)
                is Resource.Error<T, *> -> getter.call().copyWithError(it.error)
            }
        }.doOnError { setState { stateReducer(getter.call().copyWithError(it) as D) } }
                .subscribe({ data -> setState { stateReducer(data as D) } }, onError)
                .disposeOnClear()
    }
}
