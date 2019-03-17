package com.example.coreandroid.util.ext

import android.support.design.widget.BottomNavigationView

fun BottomNavigationView.checkItem(id: Int) {
    menu.findItem(id)?.isChecked = true
}

fun BottomNavigationView.setHeight(height: Int) {
    val params = this.layoutParams
    params.height = height
    this.layoutParams = params
}