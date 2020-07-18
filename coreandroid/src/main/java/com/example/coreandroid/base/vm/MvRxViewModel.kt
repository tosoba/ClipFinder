package com.example.coreandroid.base.vm

import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.withState
import com.example.core.model.Resource
import com.example.coreandroid.model.Data
import com.example.coreandroid.model.DataList
import com.example.coreandroid.model.PagedDataList
import com.example.there.domain.entity.Page
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import timber.log.Timber
import kotlin.reflect.KProperty1

open class MvRxViewModel<S : MvRxState>(
    initialState: S, debugMode: Boolean = false
) : BaseMvRxViewModel<S>(initialState, debugMode) {

    protected fun <T> Single<T>.update(
        prop: KProperty1<S, Data<T>>,
        onError: (Throwable) -> Unit = Timber::e,
        stateReducer: S.(Data<T>) -> S
    ): Disposable {
        setState { stateReducer(currentValueOf(prop).copyWithLoadingInProgress) }
        return subscribe({ data ->
            setState { stateReducer(currentValueOf(prop).copyWithNewValue(data)) }
        }, {
            setState { stateReducer(currentValueOf(prop).copyWithError(it)) }
            onError(it)
        }).disposeOnClear()
    }

    protected fun <T> Single<Resource<T>>.updateWithSingleResource(
        prop: KProperty1<S, Data<T>>,
        onError: (Throwable) -> Unit = Timber::e,
        stateReducer: S.(Data<T>) -> S
    ): Disposable {
        setState { stateReducer(currentValueOf(prop).copyWithLoadingInProgress) }
        return subscribe({
            when (it) {
                is Resource.Success -> setState {
                    stateReducer(currentValueOf(prop).copyWithNewValue(it.data))
                }
                is Resource.Error<T, *> -> setState {
                    stateReducer(currentValueOf(prop).copyWithError(it.error))
                }
            }
        }, {
            setState { stateReducer(currentValueOf(prop).copyWithError(it)) }
            onError(it)
        }).disposeOnClear()
    }

    protected fun <T> Single<Resource<T>>.updateNullableWithSingleResource(
        prop: KProperty1<S, Data<T?>>,
        onError: (Throwable) -> Unit = Timber::e,
        stateReducer: S.(Data<T?>) -> S
    ): Disposable {
        setState { stateReducer(currentValueOf(prop).copyWithLoadingInProgress) }
        return subscribe({
            setState {
                when (it) {
                    is Resource.Success -> stateReducer(
                        currentValueOf(prop)
                            .copyWithNewValue(it.data))
                    is Resource.Error<T, *> -> stateReducer(
                        currentValueOf(prop)
                            .copyWithError(it.error)
                    )
                }
            }
        }, {
            setState { stateReducer(currentValueOf(prop).copyWithError(it)) }
            onError(it)
        }).disposeOnClear()
    }

    protected fun <T> Single<Resource<Page<T>>>.updateWithPagedResource(
        prop: KProperty1<S, PagedDataList<T>>,
        onError: (Throwable) -> Unit = Timber::e,
        stateReducer: S.(PagedDataList<T>) -> S
    ): Disposable {
        setState { stateReducer(currentValueOf(prop).copyWithLoadingInProgress) }
        return subscribe({
            setState {
                when (it) {
                    is Resource.Success -> stateReducer(
                        currentValueOf(prop)
                            .copyWithNewItems(it.data.items, it.data.offset, it.data.total)
                    )
                    is Resource.Error<Page<T>, *> -> stateReducer(
                        currentValueOf(prop)
                            .copyWithError(it.error)
                    )
                }
            }
        }, {
            setState { stateReducer(currentValueOf(prop).copyWithError(it)) }
            onError(it)
        }).disposeOnClear()
    }

    protected fun <T> Single<Resource<List<T>>>.updateWithResource(
        prop: KProperty1<S, DataList<T>>,
        onError: (Throwable) -> Unit = Timber::e,
        stateReducer: S.(DataList<T>) -> S
    ): Disposable {
        setState { stateReducer(currentValueOf(prop).copyWithLoadingInProgress) }
        return subscribe({
            setState {
                when (it) {
                    is Resource.Success -> stateReducer(
                        currentValueOf(prop)
                            .copyWithNewItems(it.data)
                    )
                    is Resource.Error<List<T>, *> -> stateReducer(
                        currentValueOf(prop)
                            .copyWithError(it.error)
                    )
                }
            }
        }, {
            setState { stateReducer(currentValueOf(prop).copyWithError(it)) }
            onError(it)
        }).disposeOnClear()
    }

    protected fun <T> Observable<Resource<List<T>>>.updateWithResource(
        prop: KProperty1<S, DataList<T>>,
        onError: (Throwable) -> Unit = Timber::e,
        stateReducer: S.(DataList<T>) -> S
    ): Disposable {
        setState { stateReducer(currentValueOf(prop).copyWithLoadingInProgress) }
        return subscribe({
            setState {
                when (it) {
                    is Resource.Success -> stateReducer(
                        currentValueOf(prop)
                            .copyWithNewItems(it.data)
                    )
                    is Resource.Error<List<T>, *> -> stateReducer(
                        currentValueOf(prop)
                            .copyWithError(it.error)
                    )
                }
            }
        }, {
            setState { stateReducer(currentValueOf(prop).copyWithError(it)) }
            onError(it)
        }).disposeOnClear()
    }

    protected fun <T> current(mapper: S.() -> T): T = withState(this) { it.mapper() }

    private fun <T> currentValueOf(prop: KProperty1<S, T>): T = withState(this) { prop.get(it) }
}
