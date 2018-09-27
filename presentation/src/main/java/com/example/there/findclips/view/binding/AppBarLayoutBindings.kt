package com.example.there.findclips.view.binding

import android.databinding.BindingAdapter
import android.support.design.widget.AppBarLayout

@BindingAdapter("onOffsetChangedListener")
fun bindOnOffsetChangedListener(
        appBarLayout: AppBarLayout,
        listener: AppBarLayout.OnOffsetChangedListener
) = appBarLayout.addOnOffsetChangedListener(listener)
