package com.example.there.findclips.util.ext

import android.app.Activity
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import com.example.there.findclips.FindClipsApp
import com.example.there.findclips.R
import com.example.there.findclips.main.MainActivity

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
    setHomeAsUpIndicator(R.drawable.drawer_menu)
}

fun MainActivity.openDrawer() = findViewById<DrawerLayout>(R.id.main_drawer_layout)?.openDrawer(GravityCompat.START)

