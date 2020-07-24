package com.example.core.android.util.ext

import android.graphics.Bitmap
import com.squareup.picasso.Picasso
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

fun Picasso.getBitmapSingle(
    url: String,
    onError: (Throwable) -> Unit = Timber::e,
    onSuccess: (Bitmap) -> Unit
): Disposable = Single.create<Bitmap> {
    try {
        if (!it.isDisposed) {
            val bitmap: Bitmap = load(url).get()
            it.onSuccess(bitmap)
        }
    } catch (e: Throwable) {
        it.onError(e)
    }
}.subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe(onSuccess, onError)
