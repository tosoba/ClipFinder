package com.example.coreandroid.view.binding

import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

@BindingAdapter("foregroundDrawable")
fun bindForegroundRipple(view: FrameLayout, foregroundDrawableId: Int) {
    view.foreground = ContextCompat.getDrawable(view.context, foregroundDrawableId)
}