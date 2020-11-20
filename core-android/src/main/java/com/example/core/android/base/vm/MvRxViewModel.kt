package com.example.core.android.base.vm

import android.content.Context
import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.*
import com.example.core.android.util.ext.copyWithPaged
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

    protected fun <C, T, I> loadPaged(
        prop: KProperty1<S, DefaultLoadable<C>>,
        action: (S) -> Single<Resource<Paged<I>>>,
        subscribeOnScheduler: Scheduler? = Schedulers.io(),
        onError: (Throwable) -> Unit = ::log,
        copyWithLoading: DefaultLoadable<C>.() -> DefaultLoadable<C> = { copyWithLoadingInProgress },
        reducer: S.(DefaultLoadable<C>) -> S
    ) where C : CopyableWithPaged<T, C>,
            C : CompletionTrackable,
            I : Iterable<T> = withState { state ->
        val loadable = state.valueOf(prop)
        if (loadable !is DefaultInProgress && !loadable.value.completed) {
            action(state)
                .run { subscribeOnScheduler?.let(::subscribeOn) ?: this }
                .updateDefaultLoadableWithPagedResource(prop, onError, copyWithLoading, reducer)
        }
    }

    protected fun <C, T, I> loadPagedNoDefault(
        prop: KProperty1<S, Loadable<C>>,
        action: (S) -> Single<Resource<Paged<I>>>,
        newCopyableWithPaged: (Paged<I>) -> C,
        subscribeOnScheduler: Scheduler? = Schedulers.io(),
        onError: (Throwable) -> Unit = ::log,
        copyWithLoading: Loadable<C>.() -> Loadable<C> = { copyWithLoadingInProgress },
        reducer: S.(Loadable<C>) -> S
    ) where C : CopyableWithPaged<T, C>,
            C : CompletionTrackable,
            I : Iterable<T> = withState { state ->
        val loadable = state.valueOf(prop)
        if (loadable is LoadingInProgress || (loadable is WithValue && loadable.value.completed)) return@withState
        action(state)
            .run { subscribeOnScheduler?.let(::subscribeOn) ?: this }
            .updateLoadableWithPagedResource(prop, onError, copyWithLoading, newCopyableWithPaged, reducer)
    }

    private fun <C : CopyableWithPaged<T, C>, T, I : Iterable<T>> Single<Resource<Paged<I>>>.updateLoadableWithPagedResource(
        prop: KProperty1<S, Loadable<C>>,
        onError: (Throwable) -> Unit = ::log,
        copyWithLoading: Loadable<C>.() -> Loadable<C> = { copyWithLoadingInProgress },
        newCopyableWithPaged: (Paged<I>) -> C,
        reducer: S.(Loadable<C>) -> S
    ): Disposable {
        setState { reducer(valueOf(prop).copyWithLoading()) }
        return subscribe({
            setState {
                when (it) {
                    is Resource.Success -> reducer(when (val loadable: Loadable<C> = valueOf(prop)) {
                        is WithValue -> loadable.copyWithPaged(it.data)
                        else -> Ready(newCopyableWithPaged(it.data))
                    })
                    is Resource.Error -> {
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

    protected fun <C, T, I, Args> loadPaged(
        prop: KProperty1<S, DefaultLoadable<C>>,
        action: (S, Args) -> Single<Resource<Paged<I>>>,
        args: Args,
        subscribeOnScheduler: Scheduler? = Schedulers.io(),
        onError: (Throwable) -> Unit = ::log,
        copyWithLoading: DefaultLoadable<C>.() -> DefaultLoadable<C> = { copyWithLoadingInProgress },
        reducer: S.(DefaultLoadable<C>) -> S
    ) where C : CopyableWithPaged<T, C>,
            C : CompletionTrackable,
            I : Iterable<T> = withState { state ->
        val loadable = state.valueOf(prop)
        if (loadable !is DefaultInProgress && !loadable.value.completed) {
            action(state, args)
                .run { subscribeOnScheduler?.let(::subscribeOn) ?: this }
                .updateDefaultLoadableWithPagedResource(prop, onError, copyWithLoading, reducer)
        }
    }

    private fun <C : CopyableWithPaged<T, C>, T, I : Iterable<T>> Single<Resource<Paged<I>>>.updateDefaultLoadableWithPagedResource(
        prop: KProperty1<S, DefaultLoadable<C>>,
        onError: (Throwable) -> Unit = ::log,
        copyWithLoading: DefaultLoadable<C>.() -> DefaultLoadable<C> = { copyWithLoadingInProgress },
        reducer: S.(DefaultLoadable<C>) -> S
    ): Disposable {
        setState { reducer(valueOf(prop).copyWithLoading()) }
        return subscribe({
            setState {
                when (it) {
                    is Resource.Success -> reducer(valueOf(prop).copyWithPaged(it.data))
                    is Resource.Error -> {
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

    protected fun <T, C : Collection<T>> loadCollection(
        prop: KProperty1<S, DefaultLoadable<C>>,
        action: (S) -> Single<Resource<C>>,
        subscribeOnScheduler: Scheduler? = Schedulers.io(),
        onError: (Throwable) -> Unit = ::log,
        copyWithLoading: DefaultLoadable<C>.() -> DefaultLoadable<C> = { copyWithLoadingInProgress },
        reducer: S.(DefaultLoadable<C>) -> S
    ) = withState { state ->
        val loadable = state.valueOf(prop)
        if (loadable !is DefaultInProgress) {
            action(state)
                .run { subscribeOnScheduler?.let(::subscribeOn) ?: this }
                .updateDefaultLoadableWithCollectionResource(prop, onError, copyWithLoading, reducer)
        }
    }

    private fun <T, C : Collection<T>> Single<Resource<C>>.updateDefaultLoadableWithCollectionResource(
        prop: KProperty1<S, DefaultLoadable<C>>,
        onError: (Throwable) -> Unit = ::log,
        copyWithLoading: DefaultLoadable<C>.() -> DefaultLoadable<C> = { copyWithLoadingInProgress },
        reducer: S.(DefaultLoadable<C>) -> S
    ): Disposable {
        setState { reducer(valueOf(prop).copyWithLoading()) }
        return subscribe({
            setState {
                when (it) {
                    is Resource.Success -> reducer(DefaultReady(it.data))
                    is Resource.Error -> {
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
                    is Resource.Error<Paged<C>> -> {
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
                    is Resource.Error<C> -> {
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

    protected fun <T> load(
        prop: KProperty1<S, Loadable<T>>,
        action: (S) -> Single<Resource<T>>,
        subscribeOnScheduler: Scheduler? = Schedulers.io(),
        onError: (Throwable) -> Unit = ::log,
        reducer: S.(Loadable<T>) -> S) = withState { state ->
        if (state.valueOf(prop) !is LoadingInProgress) {
            action(state)
                .run { subscribeOnScheduler?.let(::subscribeOn) ?: this }
                .updateLoadableWithResource(prop, onError, reducer)
        }
    }

    protected fun <T, Args> load(
        prop: KProperty1<S, Loadable<T>>,
        action: (Args) -> Single<Resource<T>>,
        args: Args,
        subscribeOnScheduler: Scheduler? = Schedulers.io(),
        onError: (Throwable) -> Unit = ::log,
        reducer: S.(Loadable<T>) -> S
    ) = withState { state ->
        if (state.valueOf(prop) !is LoadingInProgress) {
            action(args)
                .run { subscribeOnScheduler?.let(::subscribeOn) ?: this }
                .updateLoadableWithResource(prop, onError, reducer)
        }
    }

    private fun <T> Single<Resource<T>>.updateLoadableWithResource(
        prop: KProperty1<S, Loadable<T>>,
        onError: (Throwable) -> Unit = ::log,
        reducer: S.(Loadable<T>) -> S
    ): Disposable {
        setState { reducer(valueOf(prop).copyWithLoadingInProgress) }
        return subscribe({
            setState {
                when (it) {
                    is Resource.Success -> reducer(Ready(it.data))
                    is Resource.Error -> {
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

    private fun <T> S.valueOf(prop: KProperty1<S, T>): T = prop.get(this)

    protected fun log(error: Throwable) = Timber.e(error, this@MvRxViewModel.javaClass.simpleName.toString())

    protected fun <T, L : BaseLoadable<T, L>> clearErrorIn(
        prop: KProperty1<S, L>,
        reducer: S.(L) -> S
    ) {
        setState { reducer(valueOf(prop).copyWithClearedError) }
    }

    protected fun Context.handleConnectivityChanges(connectedOnly: Boolean = true, block: (state: S) -> Unit) {
        observeNetworkConnectivity(connectedOnly) { withState(block) }.disposeOnClear()
    }
}
