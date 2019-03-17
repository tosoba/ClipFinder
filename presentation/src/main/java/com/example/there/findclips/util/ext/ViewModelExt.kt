package com.example.there.findclips.util.ext

import android.graphics.Bitmap
import com.squareup.picasso.Picasso

fun com.example.coreandroid.base.vm.BaseViewModel.getBitmapSingle(
        picasso: Picasso,
        url: String,
        onSuccess: (Bitmap) -> Unit,
        onError: () -> Unit
) = picasso.getBitmapSingle(url, onSuccess, onError)