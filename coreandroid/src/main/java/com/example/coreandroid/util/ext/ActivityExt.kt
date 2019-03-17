package com.example.coreandroid.util.ext

import android.app.Activity
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import com.example.coreandroid.R


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

