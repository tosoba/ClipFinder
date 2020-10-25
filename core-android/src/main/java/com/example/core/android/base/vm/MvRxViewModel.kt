package com.example.core.android.base.vm

import android.content.Context
import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.Data
import com.example.core.android.model.DataList
import com.example.core.android.model.Loading
import com.example.core.android.model.PagedDataList
import com.example.core.android.util.ext.observeNetworkConnectivity
import com.example.core.ext.castAs
import com.example.core.model.Paged
import com.example.core.model.Resource
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import kotlin.reflect.KProperty1

open class MvRxViewModel<S : MvRxState>(
    initialState: S, debugMode: Boolean = false
) : BaseMvRxViewModel<S>(initialState, debugMode) {

    protected fun <T> Single<T>.update(
        prop: KProperty1<S, Data<T>>,
        onError: (Throwable) -> Unit = ::log,
        reducer: S.(Data<T>) -> S
    ): Disposable {
        setState { reducer(valueOf(prop).copyWithLoadingInProgress) }
        return subscribe({ data ->
            setState { reducer(valueOf(prop).copyWithNewValue(data)) }
        }, {
            setState { reducer(valueOf(prop).copyWithError(it)) }
            onError(it)
        }).disposeOnClear()
    }

    protected fun <T> Single<Resource<T>>.updateWithSingleResource(
        prop: KProperty1<S, Data<T>>,
        onError: (Throwable) -> Unit = ::log,
        reducer: S.(Data<T>) -> S
    ): Disposable {
        setState { reducer(valueOf(prop).copyWithLoadingInProgress) }
        return subscribe({
            when (it) {
                is Resource.Success -> setState {
                    reducer(valueOf(prop).copyWithNewValue(it.data))
                }
                is Resource.Error<T, *> -> {
                    it.error?.castAs<Throwable>()?.let(onError)
                        ?: Timber.wtf("Unknown error")
                    setState { reducer(valueOf(prop).copyWithError(it.error)) }
                }
            }
        }, {
            setState { reducer(valueOf(prop).copyWithError(it)) }
            onError(it)
        }).disposeOnClear()
    }

    private fun <T> Single<Resource<T>>.updateNullableWithSingleResource(
        prop: KProperty1<S, Data<T?>>,
        onError: (Throwable) -> Unit = ::log,
        reducer: S.(Data<T?>) -> S
    ): Disposable {
        setState { reducer(valueOf(prop).copyWithLoadingInProgress) }
        return subscribe({
            setState {
                when (it) {
                    is Resource.Success -> reducer(valueOf(prop).copyWithNewValue(it.data))
                    is Resource.Error<T, *> -> {
                        it.error?.castAs<Throwable>()?.let(onError)
                            ?: Timber.wtf("Unknown error")
                        reducer(valueOf(prop).copyWithError(it.error))
                    }
                }
            }
        }, {
            setState { reducer(valueOf(prop).copyWithError(it)) }
            onError(it)
        }).disposeOnClear()
    }

    protected fun <C : Collection<T>, T> Single<Resource<Paged<C>>>.updateWithPagedResource(
        prop: KProperty1<S, PagedDataList<T>>,
        onError: (Throwable) -> Unit = ::log,
        shouldClear: Boolean = false,
        reducer: S.(PagedDataList<T>) -> S
    ): Disposable {
        setState {
            if (shouldClear) reducer(PagedDataList(status = Loading))
            else reducer(valueOf(prop).copyWithLoadingInProgress)
        }
        return subscribe({
            setState {
                when (it) {
                    is Resource.Success -> reducer(
                        valueOf(prop)
                            .copyWithNewItems(it.data.contents, it.data.offset, it.data.total)
                    )
                    is Resource.Error<Paged<C>, *> -> {
                        it.error?.castAs<Throwable>()?.let(onError)
                            ?: Timber.wtf("Unknown error")
                        reducer(valueOf(prop).copyWithError(it.error))
                    }
                }
            }
        }, {
            setState { reducer(valueOf(prop).copyWithError(it)) }
            onError(it)
        }).disposeOnClear()
    }

    protected fun <C : Collection<T>, T> Single<Resource<C>>.updateWithResource(
        prop: KProperty1<S, DataList<T>>,
        onError: (Throwable) -> Unit = ::log,
        reducer: S.(DataList<T>) -> S
    ): Disposable {
        setState { reducer(valueOf(prop).copyWithLoadingInProgress) }
        return subscribe({
            setState {
                when (it) {
                    is Resource.Success -> reducer(valueOf(prop).copyWithNewItems(it.data))
                    is Resource.Error<C, *> -> {
                        it.error?.castAs<Throwable>()?.let(onError)
                            ?: Timber.wtf("Unknown error")
                        reducer(valueOf(prop).copyWithError(it.error))
                    }
                }
            }
        }, {
            setState { reducer(valueOf(prop).copyWithError(it)) }
            onError(it)
        }).disposeOnClear()
    }

    protected fun <C : Collection<T>, T> load(
        prop: KProperty1<S, PagedDataList<T>>,
        action: (S) -> Single<Resource<Paged<C>>>,
        subscribeOnScheduler: Scheduler? = Schedulers.io(),
        onError: (Throwable) -> Unit = ::log,
        shouldClear: Boolean = false,
        reducer: S.(PagedDataList<T>) -> S
    ) = withState { state ->
        if (state.valueOf(prop).shouldLoadMore) {
            action(state)
                .run { subscribeOnScheduler?.let(::subscribeOn) ?: this }
                .updateWithPagedResource(prop, onError, shouldClear, reducer)
        }
    }

    protected fun <C : Collection<T>, T> load(
        prop: KProperty1<S, DataList<T>>,
        action: (S) -> Single<Resource<C>>,
        subscribeOnScheduler: Scheduler? = Schedulers.io(),
        onError: (Throwable) -> Unit = ::log,
        reducer: S.(DataList<T>) -> S
    ) = withState { state ->
        if (state.valueOf(prop).status !is Loading) {
            action(state)
                .run { subscribeOnScheduler?.let(::subscribeOn) ?: this }
                .updateWithResource(prop, onError, reducer)
        }
    }

    protected fun <T> loadNullable(
        prop: KProperty1<S, Data<T?>>,
        action: (S) -> Single<Resource<T>>,
        subscribeOnScheduler: Scheduler? = Schedulers.io(),
        onError: (Throwable) -> Unit = ::log,
        reducer: S.(Data<T?>) -> S) = withState { state ->
        if (state.valueOf(prop).status !is Loading) {
            action(state)
                .run { subscribeOnScheduler?.let(::subscribeOn) ?: this }
                .updateNullableWithSingleResource(prop, onError, reducer)
        }
    }

    protected fun <T, Args> loadNullable(
        prop: KProperty1<S, Data<T?>>,
        action: (Args) -> Single<Resource<T>>,
        args: Args,
        subscribeOnScheduler: Scheduler? = Schedulers.io(),
        onError: (Throwable) -> Unit = ::log,
        reducer: S.(Data<T?>) -> S) = withState { state ->
        if (state.valueOf(prop).status !is Loading) {
            action(args)
                .run { subscribeOnScheduler?.let(::subscribeOn) ?: this }
                .updateNullableWithSingleResource(prop, onError, reducer)
        }
    }

    private fun <T> S.valueOf(prop: KProperty1<S, T>): T = prop.get(this)

    private fun log(error: Throwable) {
        Timber.e(error, this@MvRxViewModel.javaClass.simpleName.toString())
    }

    protected fun Context.handleConnectivityChanges(connectedOnly: Boolean = true, block: (state: S) -> Unit) {
        observeNetworkConnectivity(connectedOnly) { withState(block) }.disposeOnClear()
    }
}
