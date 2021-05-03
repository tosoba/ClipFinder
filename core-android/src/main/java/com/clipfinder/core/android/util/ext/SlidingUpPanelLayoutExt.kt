package com.clipfinder.core.android.util.ext

import com.sothree.slidinguppanel.SlidingUpPanelLayout

fun SlidingUpPanelLayout.expandIfHidden() {
    if (panelState == SlidingUpPanelLayout.PanelState.HIDDEN)
        panelState = SlidingUpPanelLayout.PanelState.EXPANDED
}

fun SlidingUpPanelLayout.hideIfVisible() {
    if (panelState == SlidingUpPanelLayout.PanelState.EXPANDED ||
            panelState == SlidingUpPanelLayout.PanelState.COLLAPSED
    )
        panelState = SlidingUpPanelLayout.PanelState.HIDDEN
}

fun SlidingUpPanelLayout.showCollapsedIfHidden() {
    if (panelState == SlidingUpPanelLayout.PanelState.HIDDEN)
        panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
}
