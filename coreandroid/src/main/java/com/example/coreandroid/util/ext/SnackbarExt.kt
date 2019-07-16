package com.example.coreandroid.util.ext

import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar

fun Snackbar.showSnackbarWithBottomMargin(marginBottom: Int) {
    val params = view.layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, params.bottomMargin + marginBottom)
    view.layoutParams = params
    show()
}