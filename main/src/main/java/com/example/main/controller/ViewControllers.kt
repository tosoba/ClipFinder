package com.example.main.controller

import android.view.View
import com.example.there.findclips.util.ext.expandIfHidden
import com.example.there.findclips.util.ext.hideIfVisible
import com.sothree.slidinguppanel.SlidingUpPanelLayout

interface SlidingPanelController {
    val slidingPanel: SlidingUpPanelLayout?

    fun hideIfVisible() {
        slidingPanel?.hideIfVisible()
    }

    fun expandIfHidden() {
        slidingPanel?.expandIfHidden()
    }
}

interface NavigationDrawerController {
    fun openDrawer()
}

interface ConnectivitySnackbarHost {
    val connectivitySnackbarParentView: View?
}

interface ToolbarController {
    fun toggleToolbar()
}