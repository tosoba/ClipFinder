package com.example.there.findclips.util.ext

import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.Toolbar
import com.example.there.findclips.R
import com.example.there.findclips.main.MainActivity

fun Toolbar.setupWithBackNavigation(
        mainActivity: MainActivity?,
        onBackPressed: () -> Unit = { mainActivity?.onBackPressed() }
) {
    mainActivity?.setSupportActionBar(this)
    navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.arrow_back, null)
    setNavigationOnClickListener { onBackPressed() }
}