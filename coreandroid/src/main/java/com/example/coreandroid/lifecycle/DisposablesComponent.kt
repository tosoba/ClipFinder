package com.example.coreandroid.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class DisposablesComponent : LifecycleObserver {

    private val disposables = CompositeDisposable()

    fun add(disposable: Disposable) = disposables.add(disposable)

    fun addAll(vararg disposables: Disposable) = this.disposables.addAll(*disposables)

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun clear() = disposables.clear()
}
