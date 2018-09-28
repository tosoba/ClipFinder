package com.example.there.findclips.lifecycle

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.databinding.BaseObservable
import android.databinding.Observable

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