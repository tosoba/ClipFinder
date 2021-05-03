package com.clipfinder.core.android.util.ext

import android.os.CountDownTimer

inline fun tickingTimer(
    millisInFuture: Long,
    interval: Long,
    crossinline onTick: (Long) -> Unit
): CountDownTimer =
    object : CountDownTimer(millisInFuture, interval) {
        override fun onFinish() = Unit
        override fun onTick(millisUntilFinished: Long) = onTick(millisUntilFinished)
    }
