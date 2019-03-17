package com.example.coreandroid.lifecycle

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class DisposablesComponent : LifecycleObserver {

    private val disposables = CompositeDisposable()

    fun add(disposable: Disposable) = disposables.add(disposable)

    fun addAll(vararg disposables: Disposable) = this.disposables.addAll(*disposables)

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun clear() = disposables.clear()
}