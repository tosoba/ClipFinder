package com.example.there.findclips.activities.player

import com.google.android.youtube.player.YouTubePlayerSupportFragment
import android.view.MotionEvent
import android.widget.FrameLayout
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View


class SwipeableYTPlayerFragment: YouTubePlayerSupportFragment() {
    override fun onCreateView(layoutInflater: LayoutInflater, viewGroup: ViewGroup?, bundle: Bundle?): View? {
        val view = super.onCreateView(layoutInflater, viewGroup, bundle)
        val wrapper = object : FrameLayout(layoutInflater.context) {
            override fun onInterceptTouchEvent(ev: MotionEvent): Boolean = true
        }
        wrapper.addView(view)
        return wrapper
    }
}