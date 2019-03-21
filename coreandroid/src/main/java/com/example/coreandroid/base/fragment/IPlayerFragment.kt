package com.example.coreandroid.base.fragment

import android.view.View

interface IPlayerFragment {
    val playerView: View?

    fun onDragging() = Unit
    fun onExpanded() = Unit
    fun onCollapsed() = Unit
    fun onHidden() = Unit

    fun onPlayerDimensionsChange(slideOffset: Float) = Unit

    fun stopPlayback() = Unit
}