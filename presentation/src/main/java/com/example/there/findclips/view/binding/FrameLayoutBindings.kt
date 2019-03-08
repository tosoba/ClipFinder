package com.example.there.findclips.view.binding

import android.databinding.BindingAdapter
import android.support.v4.content.ContextCompat
import android.widget.FrameLayout

@BindingAdapter("foregroundDrawable")
fun bindForegroundRipple(view: FrameLayout, foregroundDrawableId: Int) {
    view.foreground = ContextCompat.getDrawable(view.context, foregroundDrawableId)
}