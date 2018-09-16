package com.example.there.findclips.util.ext

import com.sothree.slidinguppanel.SlidingUpPanelLayout

fun SlidingUpPanelLayout.expandIfHidden() {
    if (panelState == SlidingUpPanelLayout.PanelState.HIDDEN)
        panelState = SlidingUpPanelLayout.PanelState.EXPANDED
}