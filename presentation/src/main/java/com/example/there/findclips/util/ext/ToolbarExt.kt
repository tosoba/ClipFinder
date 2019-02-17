package com.example.there.findclips.util.ext

import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.example.there.findclips.R

fun Toolbar.setupWithBackNavigation(
        activity: AppCompatActivity?,
        onBackPressed: () -> Unit = { activity?.onBackPressed() }
) {
    activity?.setSupportActionBar(this)
    navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.arrow_back, null)
    setNavigationOnClickListener { onBackPressed() }
}