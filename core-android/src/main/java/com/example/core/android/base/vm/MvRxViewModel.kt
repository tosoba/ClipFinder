package com.example.core.android.base.vm

import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.withState
import com.example.core.android.model.Data
import com.example.core.android.model.DataList
import com.example.core.android.model.Loading
import com.example.core.android.model.PagedDataList
import com.example.core.ext.castAs
import com.example.core.model.Paged
import com.example.core.model.Resource
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
        onError: (Throwable) -> Unit = { Timber.e(it, this@MvRxViewModel.javaClass.simpleName.toString()) },
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
        onError: (Throwable) -> Unit = { Timber.e(it, this@MvRxViewModel.javaClass.simpleName.toString()) },
        stateReducer: S.(Data<T>) -> S
    ): Disposable {
        setState { stateReducer(currentValueOf(prop).copyWithLoadingInProgress) }
        return subscribe({
            when (it) {
                is Resource.Success -> setState {
                    stateReducer(currentValueOf(prop).copyWithNewValue(it.data))
                }
                is Resource.Error<T, *> -> {
                    it.error?.castAs<Throwable>()?.let(onError)
                        ?: Timber.wtf("Unknown error")
                    setState { stateReducer(currentValueOf(prop).copyWithError(it.error)) }
                }
            }
        }, {
            setState { stateReducer(currentValueOf(prop).copyWithError(it)) }
            onError(it)
        }).disposeOnClear()
    }

    protected fun <T> Single<Resource<T>>.updateNullableWithSingleResource(
        prop: KProperty1<S, Data<T?>>,
        onError: (Throwable) -> Unit = { Timber.e(it, this@MvRxViewModel.javaClass.simpleName.toString()) },
        stateReducer: S.(Data<T?>) -> S
    ): Disposable {
        setState { stateReducer(currentValueOf(prop).copyWithLoadingInProgress) }
        return subscribe({
            setState {
                when (it) {
                    is Resource.Success -> stateReducer(
                        currentValueOf(prop)
                            .copyWithNewValue(it.data))
                    is Resource.Error<T, *> -> {
                        it.error?.castAs<Throwable>()?.let(onError)
                            ?: Timber.wtf("Unknown error")
                        stateReducer(
                            currentValueOf(prop)
                                .copyWithError(it.error)
                        )
                    }
                }
            }
        }, {
            setState { stateReducer(currentValueOf(prop).copyWithError(it)) }
            onError(it)
        }).disposeOnClear()
    }

    protected fun <C : Collection<I>, I> Single<Resource<Paged<C>>>.updateWithPagedResource(
        prop: KProperty1<S, PagedDataList<I>>,
        onError: (Throwable) -> Unit = { Timber.e(it, this@MvRxViewModel.javaClass.simpleName.toString()) },
        shouldClear: Boolean = false,
        stateReducer: S.(PagedDataList<I>) -> S
    ): Disposable {
        setState {
            if (shouldClear) stateReducer(PagedDataList(status = Loading))
            else stateReducer(currentValueOf(prop).copyWithLoadingInProgress)
        }
        return subscribe({
            setState {
                when (it) {
                    is Resource.Success -> stateReducer(
                        currentValueOf(prop)
                            .copyWithNewItems(it.data.contents, it.data.offset, it.data.total)
                    )
                    is Resource.Error<Paged<C>, *> -> {
                        it.error?.castAs<Throwable>()?.let(onError)
                            ?: Timber.wtf("Unknown error")
                        stateReducer(currentValueOf(prop).copyWithError(it.error))
                    }
                }
            }
        }, {
            setState { stateReducer(currentValueOf(prop).copyWithError(it)) }
            onError(it)
        }).disposeOnClear()
    }

    protected fun <T> Single<Resource<List<T>>>.updateWithResource(
        prop: KProperty1<S, DataList<T>>,
        onError: (Throwable) -> Unit = { Timber.e(it, this@MvRxViewModel.javaClass.simpleName.toString()) },
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
                    is Resource.Error<List<T>, *> -> {
                        it.error?.castAs<Throwable>()?.let(onError)
                            ?: Timber.wtf("Unknown error")
                        stateReducer(
                            currentValueOf(prop)
                                .copyWithError(it.error)
                        )
                    }
                }
            }
        }, {
            setState { stateReducer(currentValueOf(prop).copyWithError(it)) }
            onError(it)
        }).disposeOnClear()
    }

    protected fun <T> Observable<Resource<List<T>>>.updateWithResource(
        prop: KProperty1<S, DataList<T>>,
        onError: (Throwable) -> Unit = { Timber.e(it, javaClass.simpleName.toString()) },
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
                    is Resource.Error<List<T>, *> -> {
                        it.error?.castAs<Throwable>()?.let(onError)
                            ?: Timber.wtf("Unknown error")
                        stateReducer(
                            currentValueOf(prop)
                                .copyWithError(it.error)
                        )
                    }
                }
            }
        }, {
            setState { stateReducer(currentValueOf(prop).copyWithError(it)) }
            onError(it)
        }).disposeOnClear()
    }

    private fun <T> currentValueOf(prop: KProperty1<S, T>): T = withState(this) {
        prop.get(it)
    }
}
