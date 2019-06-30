package com.example.coreandroid.util.ext

import android.graphics.Bitmap
import com.example.coreandroid.base.vm.BaseViewModel
import com.squareup.picasso.Picasso

fun BaseViewModel.getBitmapSingle(
        picasso: Picasso, url: String, onSuccess: (Bitmap) -> Unit, onError: () -> Unit) {
    picasso.getBitmapSingle(url, onSuccess, onError).disposeOnCleared()
}