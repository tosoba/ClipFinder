package com.example.coreandroid.lifecycle

import androidx.databinding.BaseObservable
import androidx.databinding.Observable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class OnPropertyChangedCallbackComponent(
        private val observable: BaseObservable,
        private val callback: Observable.OnPropertyChangedCallback
) : LifecycleObserver {

    constructor(
            observable: BaseObservable,
            onPropertyChanged: (sender: Observable?, propertyId: Int) -> Unit
    ) : this(observable, object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) = onPropertyChanged(sender, propertyId)
    })

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun bind() = observable.addOnPropertyChangedCallback(callback)

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun unbind() = observable.removeOnPropertyChangedCallback(callback)
}
