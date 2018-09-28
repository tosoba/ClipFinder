package com.example.there.findclips.util.ext

import android.support.design.widget.FloatingActionButton

fun FloatingActionButton.hideAndShow() = hide(object : FloatingActionButton.OnVisibilityChangedListener() {
    override fun onHidden(fab: FloatingActionButton?) {
        fab?.show()
    }
})