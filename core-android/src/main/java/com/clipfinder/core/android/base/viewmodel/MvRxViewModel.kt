package com.clipfinder.core.android.base.viewmodel

import android.content.Context
import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.MvRxState
import com.clipfinder.core.android.util.ext.copyWithPaged
import com.clipfinder.core.android.util.ext.loadingOrCompleted
import com.clipfinder.core.android.util.ext.observeNetworkConnectivity
import com.clipfinder.core.ext.castAs
import com.clipfinder.core.model.*
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import kotlin.reflect.KProperty1

abstract class MvRxViewModel<S : MvRxState>(
    initialState: S,
    debugMode: Boolean = false,
) : BaseMvRxViewModel<S>(initialState, debugMode) {
    protected fun <C, T, I> loadPaged(
        prop: KProperty1<S, Loadable<C>>,
        action: (S) -> Single<Resource<Paged<I>>>,
        newCopyableWithPaged: (Paged<I>) -> C,
        subscribeOnScheduler: Scheduler? = Schedulers.io(),
        onError: (Throwable) -> Unit = ::log,
        copyWithLoading: Loadable<C>.() -> Loadable<C> = { copyWithLoadingInProgress },
        reducer: S.(Loadable<C>) -> S
    ) where C : CopyableWithPaged<T, C>, C : CompletionTrackable, I : Iterable<T> =
        withState { state ->
            loadPagedWith(
                state,
                prop,
                action,
                newCopyableWithPaged,
                subscribeOnScheduler,
                onError,
                copyWithLoading,
                reducer
            )
        }

    protected fun <C, I, T> loadPagedWith(
        state: S,
        prop: KProperty1<S, Loadable<C>>,
        action: (S) -> Single<Resource<Paged<I>>>,
        newCopyableWithPaged: (Paged<I>) -> C,
        subscribeOnScheduler: Scheduler? = Schedulers.io(),
        onError: (Throwable) -> Unit = ::log,
        copyWithLoading: Loadable<C>.() -> Loadable<C> = { copyWithLoadingInProgress },
        reducer: S.(Loadable<C>) -> S
    ) where C : CopyableWithPaged<T, C>, C : CompletionTrackable, I : Iterable<T> {
        val loadable = state.valueOf(prop)
        if (loadable.loadingOrCompleted) return
        action(state)
            .run { subscribeOnScheduler?.let(::subscribeOn) ?: this }
            .updateLoadableWithPagedResource(
                prop,
                onError,
                copyWithLoading,
                newCopyableWithPaged,
                reducer
            )
    }

    private fun <C : CopyableWithPaged<T, C>, T, I : Iterable<T>> Single<Resource<Paged<I>>>
        .updateLoadableWithPagedResource(
        prop: KProperty1<S, Loadable<C>>,
        onError: (Throwable) -> Unit = ::log,
        copyWithLoading: Loadable<C>.() -> Loadable<C> = { copyWithLoadingInProgress },
        newCopyableWithPaged: (Paged<I>) -> C,
        reducer: S.(Loadable<C>) -> S
    ): Disposable {
        setState { reducer(valueOf(prop).copyWithLoading()) }
        return subscribe(
                { resource ->
                    setState {
                        when (resource) {
                            is Resource.Success ->
                                reducer(copyWithPaged(resource.data, prop, newCopyableWithPaged))
                            is Resource.Error -> {
                                resource.error?.castAs<Throwable>()?.let(onError)
                                    ?: run { logResourceError(resource) }
                                reducer(valueOf(prop).copyWithError(resource.error))
                            }
                        }
                    }
                },
                {
                    setState { reducer(valueOf(prop).copyWithError(it)) }
                    onError(it)
                }
            )
            .disposeOnClear()
    }

    protected fun <C : CopyableWithPaged<T, C>, T, I : Iterable<T>> S.copyWithPaged(
        paged: Paged<I>,
        prop: KProperty1<S, Loadable<C>>,
        newCopyableWithPaged: (Paged<I>) -> C
    ): Loadable<C> =
        when (val loadable = valueOf(prop)) {
            is WithValue -> loadable.copyWithPaged(paged)
            else -> Ready(newCopyableWithPaged(paged))
        }

    protected fun <C, T, I, Args> loadPaged(
        prop: KProperty1<S, Loadable<C>>,
        action: (S, Args) -> Single<Resource<Paged<I>>>,
        args: Args,
        newCopyableWithPaged: (Paged<I>) -> C,
        subscribeOnScheduler: Scheduler? = Schedulers.io(),
        onError: (Throwable) -> Unit = ::log,
        copyWithLoading: Loadable<C>.() -> Loadable<C> = { copyWithLoadingInProgress },
        reducer: S.(Loadable<C>) -> S
    ) where C : CopyableWithPaged<T, C>, C : CompletionTrackable, I : Iterable<T> =
        withState { state ->
            val loadable = state.valueOf(prop)
            if (loadable.loadingOrCompleted) return@withState
            action(state, args)
                .run { subscribeOnScheduler?.let(::subscribeOn) ?: this }
                .updateDefaultLoadableWithPagedResource(
                    prop,
                    onError,
                    copyWithLoading,
                    newCopyableWithPaged,
                    reducer
                )
        }

    private fun <C : CopyableWithPaged<T, C>, T, I : Iterable<T>> Single<Resource<Paged<I>>>
        .updateDefaultLoadableWithPagedResource(
        prop: KProperty1<S, Loadable<C>>,
        onError: (Throwable) -> Unit = ::log,
        copyWithLoading: Loadable<C>.() -> Loadable<C> = { copyWithLoadingInProgress },
        newCopyableWithPaged: (Paged<I>) -> C,
        reducer: S.(Loadable<C>) -> S
    ): Disposable {
        setState { reducer(valueOf(prop).copyWithLoading()) }
        return subscribe(
                { resource ->
                    setState {
                        when (resource) {
                            is Resource.Success ->
                                reducer(copyWithPaged(resource.data, prop, newCopyableWithPaged))
                            is Resource.Error -> {
                                resource.error?.castAs<Throwable>()?.let(onError)
                                    ?: run { logResourceError(resource) }
                                reducer(valueOf(prop).copyWithError(resource.error))
                            }
                        }
                    }
                },
                {
                    setState { reducer(valueOf(prop).copyWithError(it)) }
                    onError(it)
                }
            )
            .disposeOnClear()
    }

    private fun logResourceError(resource: Resource.Error<*>) {
        resource.error?.let { Timber.e(it.toString()) }
        resource.data?.let { Timber.e(it.toString()) }
        if (resource.error == null && resource.data == null) {
            Timber.wtf("Unknown error")
        }
    }

    protected fun <T, C : Collection<T>> loadCollection(
        prop: KProperty1<S, Loadable<C>>,
        action: (S) -> Single<Resource<C>>,
        subscribeOnScheduler: Scheduler? = Schedulers.io(),
        onError: (Throwable) -> Unit = ::log,
        copyWithLoading: Loadable<C>.() -> Loadable<C> = { copyWithLoadingInProgress },
        reducer: S.(Loadable<C>) -> S
    ) = withState { state ->
        val loadable = state.valueOf(prop)
        if (loadable is LoadingInProgress) return@withState
        action(state)
            .run { subscribeOnScheduler?.let(::subscribeOn) ?: this }
            .updateLoadableWithCollectionResource(prop, onError, copyWithLoading, reducer)
    }

    protected fun <T, C : Collection<T>, Args> loadCollection(
        prop: KProperty1<S, Loadable<C>>,
        action: (S, Args) -> Single<Resource<C>>,
        args: Args,
        subscribeOnScheduler: Scheduler? = Schedulers.io(),
        onError: (Throwable) -> Unit = ::log,
        copyWithLoading: Loadable<C>.() -> Loadable<C> = { copyWithLoadingInProgress },
        reducer: S.(Loadable<C>) -> S
    ) = withState { state ->
        val loadable = state.valueOf(prop)
        if (loadable is LoadingInProgress) return@withState
        action(state, args)
            .run { subscribeOnScheduler?.let(::subscribeOn) ?: this }
            .updateLoadableWithCollectionResource(prop, onError, copyWithLoading, reducer)
    }

    protected fun <T, C : Collection<T>> Single<Resource<C>>.updateLoadableWithCollectionResource(
        prop: KProperty1<S, Loadable<C>>,
        onError: (Throwable) -> Unit = ::log,
        copyWithLoading: Loadable<C>.() -> Loadable<C> = { copyWithLoadingInProgress },
        reducer: S.(Loadable<C>) -> S
    ): Disposable {
        setState { reducer(valueOf(prop).copyWithLoading()) }
        return subscribe(
                {
                    setState {
                        when (it) {
                            is Resource.Success -> reducer(Ready(it.data))
                            is Resource.Error -> {
                                it.error?.castAs<Throwable>()?.let(onError)
                                    ?: run { logResourceError(it) }
                                reducer(valueOf(prop).copyWithError(it.error))
                            }
                        }
                    }
                },
                {
                    setState { reducer(valueOf(prop).copyWithError(it)) }
                    onError(it)
                }
            )
            .disposeOnClear()
    }

    protected fun <T> load(
        prop: KProperty1<S, Loadable<T>>,
        action: (S) -> Single<Resource<T>>,
        subscribeOnScheduler: Scheduler? = Schedulers.io(),
        onError: (Throwable) -> Unit = ::log,
        reducer: S.(Loadable<T>) -> S
    ) = withState { state ->
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
        return subscribe(
                {
                    setState {
                        when (it) {
                            is Resource.Success -> reducer(Ready(it.data))
                            is Resource.Error -> {
                                it.error?.castAs<Throwable>()?.let(onError)
                                    ?: run { logResourceError(it) }
                                reducer(valueOf(prop).copyWithError(it.error))
                            }
                        }
                    }
                },
                {
                    setState { reducer(valueOf(prop).copyWithError(it)) }
                    onError(it)
                }
            )
            .disposeOnClear()
    }

    private fun <T> S.valueOf(prop: KProperty1<S, T>): T = prop.get(this)

    protected fun log(error: Throwable) =
        Timber.e(error, this@MvRxViewModel.javaClass.simpleName.toString())

    protected fun <T> clearErrorIn(
        prop: KProperty1<S, Loadable<T>>,
        reducer: S.(Loadable<T>) -> S
    ) {
        setState { reducer(valueOf(prop).copyWithClearedError) }
    }

    protected fun Context.handleConnectivityChanges(
        connectedOnly: Boolean = true,
        block: (state: S) -> Unit
    ) {
        observeNetworkConnectivity(connectedOnly) { withState(block) }.disposeOnClear()
    }
}
