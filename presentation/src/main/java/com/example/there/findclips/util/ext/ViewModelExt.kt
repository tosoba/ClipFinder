package com.example.there.findclips.util.ext

import android.graphics.Bitmap
import com.example.there.findclips.base.vm.BaseViewModel
import com.squareup.picasso.Picasso

fun BaseViewModel.getBitmapSingle(
        picasso: Picasso,
        url: String,
        onSuccess: (Bitmap) -> Unit,
        onError: () -> Unit
) = addDisposable(picasso.getBitmapSingle(url, onSuccess, onError))