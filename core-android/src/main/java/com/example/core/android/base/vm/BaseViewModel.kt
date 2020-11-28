package com.example.core.android.base.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.clipfinder.core.model.Resource
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

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

    protected fun <T> Maybe<T>.subscribeAndDisposeOnCleared(onNext: (T) -> Unit, onError: (Throwable) -> Unit) {
        subscribe(onNext, onError).disposeOnCleared()
    }

    private fun Disposable.disposeOnCleared() {
        compositeDisposable.add(this)
    }

    fun <T> Single<Resource<T>>.takeSuccessOnly(
        onError: ((Resource.Error<T>) -> Unit)? = {
            Timber.e(javaClass.simpleName, it.error?.toString() ?: "Unknown error")
        }
    ): Maybe<T> = run {
        if (onError != null) {
            this.doOnSuccess {
                if (it is Resource.Error<T>) onError(it)
            }
        } else this
    }.filter {
        it is Resource.Success<T>
    }.map {
        (it as Resource.Success<T>).data
    }
}