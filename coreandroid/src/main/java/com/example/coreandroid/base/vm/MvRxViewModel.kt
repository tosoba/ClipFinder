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

open class MvRxViewModel<S : MvRxState>(
        initialState: S, debugMode: Boolean = false
) : BaseMvRxViewModel<S>(initialState, debugMode) {

    protected fun <T> Single<T>.update(
            current: () -> Data<T>,
            onError: (Throwable) -> Unit = {},
            stateReducer: S.(Data<T>) -> S
    ): Disposable {
        setState { stateReducer(current().copyWithLoadingInProgress) }
        return map { current().copyWithNewValue(it) }
                .doOnError { setState { stateReducer(current().copyWithError(it)) } }
                .subscribe({ data -> setState { stateReducer(data) } }, onError)
                .disposeOnClear()
    }

    protected fun <T> Single<Resource<ListPage<T>>>.updateWithPagedResource(
            current: () -> PagedDataList<T>,
            onError: (Throwable) -> Unit = {},
            stateReducer: S.(PagedDataList<T>) -> S
    ): Disposable {
        setState { stateReducer(current().copyWithLoadingInProgress) }
        return map {
            when (it) {
                is Resource.Success -> current().copyWithNewItems(
                        it.data.items, it.data.offset, it.data.totalItems
                )
                is Resource.Error<ListPage<T>, *> -> current().copyWithError(it.error)
            }
        }.doOnError { setState { stateReducer(current().copyWithError(it)) } }
                .subscribe({ data -> setState { stateReducer(data) } }, onError)
                .disposeOnClear()
    }

    protected fun <T> Observable<Resource<List<T>>>.updateWithResource(
            current: () -> DataList<T>,
            onError: (Throwable) -> Unit = {},
            stateReducer: S.(DataList<T>) -> S
    ): Disposable {
        setState { stateReducer(current().copyWithLoadingInProgress) }
        return map {
            when (it) {
                is Resource.Success -> current().copyWithNewItems(it.data)
                is Resource.Error<List<T>, *> -> current().copyWithError(it.error)
            }
        }.doOnError { setState { stateReducer(current().copyWithError(it)) } }
                .subscribe({ data -> setState { stateReducer(data) } }, onError)
                .disposeOnClear()
    }

    protected inline fun <T> current(crossinline mapper: S.() -> T): () -> T = {
        withState(this) { it.mapper() }
    }
}
