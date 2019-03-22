package com.example.there.findclips.util.ext

import android.app.Activity
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager

fun Activity.registerFragmentLifecycleCallbacks(
        callbacks: FragmentManager.FragmentLifecycleCallbacks,
        recursive: Boolean
) = (this as? FragmentActivity)
        ?.supportFragmentManager
        ?.registerFragmentLifecycleCallbacks(callbacks, recursive)


