package com.clipfinder.core.android.util.ext

import com.google.android.material.bottomnavigation.BottomNavigationView

fun BottomNavigationView.checkItem(id: Int) {
    menu.findItem(id)?.isChecked = true
}

fun BottomNavigationView.setHeight(height: Int) {
    val params = this.layoutParams
    params.height = height
    this.layoutParams = params
}
