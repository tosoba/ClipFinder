package com.example.coreandroid.util.ext

import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.example.coreandroid.R

fun Toolbar.setupWithBackNavigation(
        activity: AppCompatActivity?,
        onBackPressed: () -> Unit = { activity?.onBackPressed() }
) {
    activity?.setSupportActionBar(this)
    navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.arrow_back, null)
    setNavigationOnClickListener { onBackPressed() }
}