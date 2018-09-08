package com.example.there.findclips.util.ext

import android.animation.ValueAnimator
import android.view.View

fun View.animateHeight(startHeight: Int, endHeight: Int, duration: Long) {
    val animator = ValueAnimator.ofInt(startHeight, endHeight)
    animator.addUpdateListener { animation ->
        val animatedVal = animation.animatedValue as Int
        val params = this.layoutParams
        params.height = animatedVal
        this.layoutParams = params
    }
    animator.duration = duration
    animator.start()
}