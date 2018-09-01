package com.example.there.findclips.util.ext

import android.support.design.widget.BottomNavigationView

fun BottomNavigationView.checkItem(id: Int) {
    menu.findItem(id)?.isChecked = true
}