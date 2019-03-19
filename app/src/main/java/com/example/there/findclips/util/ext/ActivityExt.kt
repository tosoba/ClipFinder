package com.example.there.findclips.util.ext

import android.app.Activity
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import com.example.there.findclips.FindClipsApp
import com.example.there.findclips.R

val Activity.app: FindClipsApp
    get() = application as FindClipsApp

fun Activity.registerFragmentLifecycleCallbacks(
        callbacks: FragmentManager.FragmentLifecycleCallbacks,
        recursive: Boolean
) = (this as? FragmentActivity)
        ?.supportFragmentManager
        ?.registerFragmentLifecycleCallbacks(callbacks, recursive)

fun AppCompatActivity.showDrawerHamburger() = supportActionBar?.apply {
    setDisplayHomeAsUpEnabled(true)
    setHomeAsUpIndicator(R.drawable.spotify_drawer_menu_background)
}

