package com.example.coreandroid.util.ext

import android.graphics.Bitmap
import com.example.coreandroid.base.vm.BaseViewModel
import com.squareup.picasso.Picasso
import timber.log.Timber

fun BaseViewModel.getBitmapSingle(
    picasso: Picasso,
    url: String,
    onError: (Throwable) -> Unit = Timber::e,
    onSuccess: (Bitmap) -> Unit
) {
    picasso.getBitmapSingle(url, onError, onSuccess).disposeOnCleared()
}