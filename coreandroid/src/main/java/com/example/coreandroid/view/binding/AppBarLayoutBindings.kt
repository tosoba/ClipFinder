package com.example.coreandroid.view.binding

import androidx.databinding.BindingAdapter
import com.google.android.material.appbar.AppBarLayout

@BindingAdapter("onOffsetChangedListener")
fun bindOnOffsetChangedListener(
        appBarLayout: AppBarLayout,
        listener: AppBarLayout.OnOffsetChangedListener
) = appBarLayout.addOnOffsetChangedListener(listener)
