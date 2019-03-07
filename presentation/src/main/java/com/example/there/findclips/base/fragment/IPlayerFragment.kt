package com.example.there.findclips.base.fragment

interface IPlayerFragment : StopsPlayback, ReactsToSlidingPanelStateChanges

interface ReactsToSlidingPanelStateChanges {
    fun onDragging() = Unit
    fun onExpanded() = Unit
    fun onCollapsed() = Unit
    fun onHidden() = Unit
}

interface StopsPlayback {
    fun stopPlayback() = Unit
}