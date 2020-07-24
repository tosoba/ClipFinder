package com.example.core.android.util.ext

import com.google.android.material.floatingactionbutton.FloatingActionButton

fun FloatingActionButton.hideAndShow() = hide(object : FloatingActionButton.OnVisibilityChangedListener() {
    override fun onHidden(fab: FloatingActionButton?) {
        fab?.show()
    }
})