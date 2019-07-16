package com.example.coreandroid.util.ext

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.example.coreandroid.R


fun Activity.registerFragmentLifecycleCallbacks(
        callbacks: androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks,
        recursive: Boolean
) = (this as? androidx.fragment.app.FragmentActivity)
        ?.supportFragmentManager
        ?.registerFragmentLifecycleCallbacks(callbacks, recursive)

fun AppCompatActivity.showDrawerHamburger() = supportActionBar?.apply {
    setDisplayHomeAsUpEnabled(true)
    setHomeAsUpIndicator(R.drawable.spotify_drawer_menu_background)
}

