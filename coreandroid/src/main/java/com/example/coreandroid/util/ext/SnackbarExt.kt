package com.example.coreandroid.util.ext

import android.support.design.widget.Snackbar
import android.view.ViewGroup

fun Snackbar.showSnackbarWithBottomMargin(marginBottom: Int) {
    val params = view.layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, params.bottomMargin + marginBottom)
    view.layoutParams = params
    show()
}