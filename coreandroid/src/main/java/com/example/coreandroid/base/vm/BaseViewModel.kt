package com.example.coreandroid.base.vm

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.core.model.Resource
import io.reactivex.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    val errorState: MutableLiveData<Throwable?> = MutableLiveData()

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    protected open fun onError(t: Throwable) {
        errorState.value = t
    }

    protected inline fun getOnErrorWith(crossinline block: () -> Unit): (Throwable) -> Unit = {
        block()
        onError(it)
    }

    protected fun <T> Maybe<T>.subscribeAndDisposeOnCleared() {
        subscribe().disposeOnCleared()
    }

    protected fun <T> Maybe<T>.subscribeAndDisposeOnCleared(onNext: (T) -> Unit) {
        subscribe(onNext).disposeOnCleared()
    }

    protected fun <T> Maybe<T>.subscribeAndDisposeOnCleared(onNext: (T) -> Unit, onError: (Throwable) -> Unit) {
        subscribe(onNext, onError).disposeOnCleared()
    }

    protected fun <T> Single<T>.subscribeAndDisposeOnCleared() {
        subscribe().disposeOnCleared()
    }

    protected fun <T> Single<T>.subscribeAndDisposeOnCleared(onNext: (T) -> Unit) {
        subscribe(onNext).disposeOnCleared()
    }

    protected fun <T> Single<T>.subscribeAndDisposeOnCleared(onNext: (T) -> Unit, onError: (Throwable) -> Unit) {
        subscribe(onNext, onError).disposeOnCleared()
    }

    protected fun <T> Observable<T>.subscribeAndDisposeOnCleared() {
        subscribe().disposeOnCleared()
    }

    protected fun <T> Observable<T>.subscribeAndDisposeOnCleared(onNext: (T) -> Unit) {
        subscribe(onNext).disposeOnCleared()
    }

    protected fun <T> Observable<T>.subscribeAndDisposeOnCleared(onNext: (T) -> Unit, onError: (Throwable) -> Unit) {
        subscribe(onNext, onError).disposeOnCleared()
    }

    protected fun <T> Flowable<T>.subscribeAndDisposeOnCleared() {
        subscribe().disposeOnCleared()
    }

    protected fun <T> Flowable<T>.subscribeAndDisposeOnCleared(onNext: (T) -> Unit) {
        subscribe(onNext).disposeOnCleared()
    }

    protected fun <T> Flowable<T>.subscribeAndDisposeOnCleared(onNext: (T) -> Unit, onError: (Throwable) -> Unit) {
        subscribe(onNext, onError).disposeOnCleared()
    }

    protected fun Completable.subscribeAndDisposeOnCleared() {
        subscribe().disposeOnCleared()
    }

    protected fun Completable.subscribeAndDisposeOnCleared(onComplete: () -> Unit) {
        subscribe(onComplete).disposeOnCleared()
    }

    protected fun Completable.subscribeAndDisposeOnCleared(onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        subscribe(onSuccess, onError).disposeOnCleared()
    }

    fun Disposable.disposeOnCleared() {
        compositeDisposable.add(this)
    }

    fun <T> Observable<Resource<T>>.takeSuccessOnly(
            onError: ((Resource.Error<T, *>) -> Unit)? = {
                Log.e(javaClass.simpleName, it.error?.toString() ?: "Unknown error")
            }
    ): Observable<T> = run {
        if (onError != null) {
            this.doOnNext {
                if (it is Resource.Error<T, *>) onError(it)
            }
        } else this
    }.filter { it is Resource.Success<T> }.map { (it as Resource.Success<T>).data }

    fun <T> Flowable<Resource<T>>.takeSuccessOnly(
            onError: ((Resource.Error<T, *>) -> Unit)? = {
                Log.e(javaClass.simpleName, it.error?.toString() ?: "Unknown error")
            }
    ): Flowable<T> = run {
        if (onError != null) {
            this.doOnNext {
                if (it is Resource.Error<T, *>) onError(it)
            }
        } else this
    }.filter { it is Resource.Success<T> }.map { (it as Resource.Success<T>).data }

    fun <T> Single<Resource<T>>.takeSuccessOnly(
            onError: ((Resource.Error<T, *>) -> Unit)? = {
                Log.e(javaClass.simpleName, it.error?.toString() ?: "Unknown error")
            }
    ): Maybe<T> = run {
        if (onError != null) {
            this.doOnSuccess {
                if (it is Resource.Error<T, *>) onError(it)
            }
        } else this
    }.filter { it is Resource.Success<T> }.map { (it as Resource.Success<T>).data }
}