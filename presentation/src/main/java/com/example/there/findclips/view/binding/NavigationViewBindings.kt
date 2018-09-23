package com.example.there.findclips.view.binding

import android.databinding.BindingAdapter
import android.support.design.widget.NavigationView

@BindingAdapter("navigationItemSelectedListener")
fun bindOnNavigationItemSelectedListener(bottomNavigationView: NavigationView, listener: NavigationView.OnNavigationItemSelectedListener) {
    bottomNavigationView.setNavigationItemSelectedListener(listener)
}