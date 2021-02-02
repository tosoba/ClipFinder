package com.clipfinder.core.android.util.ext

import android.view.View
import com.squareup.picasso.Picasso
import io.reactivex.disposables.Disposable

fun View.loadBackgroundGradient(url: String): Disposable = Picasso.with(context)
    .getBitmapSingle(url) { bitmap ->
        bitmap.generateColorGradient {
            background = it
            invalidate()
        }
    }

fun View.showIfHidden() {
    if (visibility == View.GONE) visibility = View.VISIBLE
}

fun View.hideIfShowing() {
    if (visibility == View.VISIBLE) visibility = View.GONE
}
