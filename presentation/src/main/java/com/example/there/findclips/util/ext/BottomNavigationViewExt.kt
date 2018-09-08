package com.example.there.findclips.util.ext

import android.support.design.widget.BottomNavigationView

fun BottomNavigationView.checkItem(id: Int) {
    menu.findItem(id)?.isChecked = true
}

fun BottomNavigationView.setHeight(height: Int) {
    val params = this.layoutParams
    params.height = height
    this.layoutParams = params
}