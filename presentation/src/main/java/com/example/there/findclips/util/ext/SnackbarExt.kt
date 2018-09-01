package com.example.there.findclips.util.ext

import android.support.design.widget.Snackbar
import android.widget.FrameLayout

fun Snackbar.showSnackbarWithBottomMargin(marginBottom: Int) {
    val params = view.layoutParams as FrameLayout.LayoutParams
    params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, params.bottomMargin + marginBottom)
    view.layoutParams = params
    show()
}