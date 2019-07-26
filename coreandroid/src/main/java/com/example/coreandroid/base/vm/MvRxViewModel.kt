package com.example.coreandroid.base.vm

import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.withState
import com.example.core.model.Resource
import com.example.coreandroid.model.Data
import com.example.coreandroid.model.DataList
import com.example.coreandroid.model.PagedDataList
import com.example.there.domain.entity.ListPage
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import kotlin.reflect.KProperty1

open class MvRxViewModel<S : MvRxState>(
        initialState: S, debugMode: Boolean = false
) : BaseMvRxViewModel<S>(initialState, debugMode) {

    protected fun <T> Single<T>.update(
            prop: KProperty1<S, Data<T>>,
            onError: (Throwable) -> Unit = {},
            stateReducer: S.(Data<T>) -> S
    ): Disposable {
        setState { stateReducer(currentValueOf(prop).copyWithLoadingInProgress) }
        return map { currentValueOf(prop).copyWithNewValue(it) }
                .doOnError { setState { stateReducer(currentValueOf(prop).copyWithError(it)) } }
                .subscribe({ data -> setState { stateReducer(data) } }, onError)
                .disposeOnClear()
    }

    protected fun <T> Single<Resource<ListPage<T>>>.updateWithPagedResource(
            prop: KProperty1<S, PagedDataList<T>>,
            onError: (Throwable) -> Unit = {},
            stateReducer: S.(PagedDataList<T>) -> S
    ): Disposable {
        setState { stateReducer(currentValueOf(prop).copyWithLoadingInProgress) }
        return map {
            when (it) {
                is Resource.Success -> currentValueOf(prop).copyWithNewItems(
                        it.data.items, it.data.offset, it.data.totalItems
                )
                is Resource.Error<ListPage<T>, *> -> currentValueOf(prop).copyWithError(it.error)
            }
        }.doOnError { setState { stateReducer(currentValueOf(prop).copyWithError(it)) } }
                .subscribe({ data -> setState { stateReducer(data) } }, onError)
                .disposeOnClear()
    }

    protected fun <T> Observable<Resource<List<T>>>.updateWithResource(
            prop: KProperty1<S, DataList<T>>,
            onError: (Throwable) -> Unit = {},
            stateReducer: S.(DataList<T>) -> S
    ): Disposable {
        setState { stateReducer(currentValueOf(prop).copyWithLoadingInProgress) }
        return map {
            when (it) {
                is Resource.Success -> currentValueOf(prop).copyWithNewItems(it.data)
                is Resource.Error<List<T>, *> -> currentValueOf(prop).copyWithError(it.error)
            }
        }.doOnError { setState { stateReducer(currentValueOf(prop).copyWithError(it)) } }
                .subscribe({ data -> setState { stateReducer(data) } }, onError)
                .disposeOnClear()
    }

    protected fun <T> currentValueOf(prop: KProperty1<S, T>): T = withState(this) { prop.get(it) }
}
