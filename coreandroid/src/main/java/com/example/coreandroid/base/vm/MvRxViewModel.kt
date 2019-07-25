package com.example.coreandroid.base.vm

import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.MvRxState
import com.example.core.model.Resource
import com.example.coreandroid.model.Data
import com.example.coreandroid.model.DataList
import com.example.coreandroid.model.PagedDataList
import com.example.there.domain.entity.ListPage
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import kotlin.reflect.KProperty

open class MvRxViewModel<S : MvRxState>(
        initialState: S, debugMode: Boolean = false
) : BaseMvRxViewModel<S>(initialState, debugMode) {

    protected fun <T> Single<T>.update(
            getter: KProperty<Data<T>>,
            onError: (Throwable) -> Unit = {},
            stateReducer: S.(Data<T>) -> S
    ): Disposable {
        setState { stateReducer(getter.call().copyWithLoadingInProgress) }
        return map { getter.call().success(it) }
                .doOnError { setState { stateReducer(getter.call().copyWithError(it)) } }
                .subscribe({ data -> setState { stateReducer(data) } }, onError)
                .disposeOnClear()
    }

    protected fun <T> Single<Resource<ListPage<T>>>.updateWithPagedResource(
            getter: KProperty<PagedDataList<T>>,
            onError: (Throwable) -> Unit = {},
            stateReducer: S.(PagedDataList<T>) -> S
    ): Disposable {
        setState { stateReducer(getter.call().copyWithLoadingInProgress) }
        return map {
            when (it) {
                is Resource.Success -> getter.call().copyWithNewItems(
                        it.data.items, it.data.offset, it.data.totalItems
                )
                is Resource.Error<ListPage<T>, *> ->
                    getter.call().copyWithError(it.error)
            }
        }.doOnError { setState { stateReducer(getter.call().copyWithError(it)) } }
                .subscribe({ data -> setState { stateReducer(data) } }, onError)
                .disposeOnClear()
    }

    @Suppress("UNCHECKED_CAST")
    protected fun <T> Observable<Resource<List<T>>>.updateWithResource(
            getter: KProperty<DataList<T>>,
            onError: (Throwable) -> Unit = {},
            stateReducer: S.(DataList<T>) -> S
    ): Disposable {
        setState { stateReducer(getter.call().copyWithLoadingInProgress) }
        return map {
            when (it) {
                is Resource.Success -> getter.call().copyWithNewItems(it.data)
                is Resource.Error<List<T>, *> -> getter.call().copyWithError(it.error)
            }
        }.doOnError { setState { stateReducer(getter.call().copyWithError(it)) } }
                .subscribe({ data -> setState { stateReducer(data) } }, onError)
                .disposeOnClear()
    }
}
