package com.example.coreandroid.util.ext

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T : Any> LiveData<T>.observeIgnoringNulls(
        owner: LifecycleOwner,
        onNext: (T) -> Unit
) = observe(owner, Observer {
    if (it != null) onNext(it)
})
