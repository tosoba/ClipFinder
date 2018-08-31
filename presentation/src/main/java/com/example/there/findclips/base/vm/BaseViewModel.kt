package com.example.there.findclips.base.vm

import android.arch.lifecycle.ViewModel
import com.example.there.findclips.util.rx.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    protected fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    protected fun clearDisposables() {
        compositeDisposable.clear()
    }

    override fun onCleared() {
        clearDisposables()
    }

    val errorState: SingleLiveEvent<Throwable?> = SingleLiveEvent()

    protected open fun onError(t: Throwable) {
        errorState.value = t
    }
}