package com.example.core.android.view.binding

import androidx.databinding.BindingAdapter
import com.google.android.material.navigation.NavigationView

@BindingAdapter("navigationItemSelectedListener")
fun bindOnNavigationItemSelectedListener(bottomNavigationView: NavigationView, listener: NavigationView.OnNavigationItemSelectedListener) {
    bottomNavigationView.setNavigationItemSelectedListener(listener)
}
