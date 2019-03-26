package com.example.coreandroid.util.ext

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer

fun <T : Any> LiveData<T>.observeIgnoringNulls(
        owner: LifecycleOwner,
        onNext: (T) -> Unit
) = observe(owner, Observer {
    if (it != null) onNext(it)
})
