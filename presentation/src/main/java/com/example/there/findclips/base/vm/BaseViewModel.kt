package com.example.there.findclips.base.vm

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
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

    protected fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }
}