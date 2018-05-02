package com.example.there.findclips.bindings

import android.databinding.BindingAdapter
import android.support.design.widget.BottomNavigationView

@BindingAdapter("onNavigationItemSelectedListener")
fun bindOnNavigationItemSelectedListener(bottomNavigationView: BottomNavigationView, listener: BottomNavigationView.OnNavigationItemSelectedListener) {
    bottomNavigationView.setOnNavigationItemSelectedListener(listener)
}