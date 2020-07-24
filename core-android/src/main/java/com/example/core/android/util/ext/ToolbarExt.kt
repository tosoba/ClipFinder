package com.example.core.android.util.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import com.example.core.android.R

fun Toolbar.setupWithBackNavigation(
    activity: AppCompatActivity?,
    onBackPressed: () -> Unit = { activity?.onBackPressed() }
) {
    activity?.setSupportActionBar(this)
    navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.arrow_back, null)
    setNavigationOnClickListener { onBackPressed() }
}
