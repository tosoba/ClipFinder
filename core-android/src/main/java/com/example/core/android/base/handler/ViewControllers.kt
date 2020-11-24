package com.example.core.android.base.handler

import com.example.core.android.util.ext.expandIfHidden
import com.example.core.android.util.ext.hideIfVisible
import com.example.core.android.util.ext.showCollapsedIfHidden
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

interface NavigationDrawerController {
    fun openDrawer()
}

interface ToolbarController {
    fun toggleToolbar()
}
