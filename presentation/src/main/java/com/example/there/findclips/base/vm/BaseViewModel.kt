package com.example.there.findclips.base.vm

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
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

    protected fun Disposable.disposeOnCleared() {
        compositeDisposable.add(this)
    }
}