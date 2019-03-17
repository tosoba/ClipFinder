package com.example.coreandroid.view.binding

import android.databinding.BindingAdapter
import android.view.View
import com.sothree.slidinguppanel.SlidingUpPanelLayout

@BindingAdapter("panelSlideListener")
fun bindPanelSlideListener(slidingUpPanelLayout: SlidingUpPanelLayout, listener: SlidingUpPanelLayout.PanelSlideListener) {
    slidingUpPanelLayout.addPanelSlideListener(listener)
}

@BindingAdapter("fadeOnClickListener")
fun setFadeOnClickListener(slidingUpPanelLayout: SlidingUpPanelLayout, listener: View.OnClickListener) {
    slidingUpPanelLayout.setFadeOnClickListener(listener)
}

@BindingAdapter("initialSlideState")
fun setInitialPanelSlideState(slidingUpPanelLayout: SlidingUpPanelLayout, state: SlidingUpPanelLayout.PanelState) {
    slidingUpPanelLayout.panelState = state
}