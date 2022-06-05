package com.clipfinder.core.android.base.handler

import com.clipfinder.core.android.util.ext.expandIfHidden
import com.clipfinder.core.android.util.ext.hideIfVisible
import com.clipfinder.core.android.util.ext.showCollapsedIfHidden
import com.sothree.slidinguppanel.SlidingUpPanelLayout

interface SlidingPanelController {
    val slidingPanel: SlidingUpPanelLayout?

    fun hideIfVisible() {
        slidingPanel?.hideIfVisible()
    }

    fun expandIfHidden() {
        slidingPanel?.expandIfHidden()
    }

    fun showCollapsedIfHidden() {
        slidingPanel?.showCollapsedIfHidden()
    }
}
