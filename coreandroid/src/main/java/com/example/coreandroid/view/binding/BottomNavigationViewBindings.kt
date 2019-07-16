package com.example.coreandroid.view.binding

import androidx.databinding.BindingAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

@BindingAdapter("onNavigationItemSelectedListener")
fun bindOnNavigationItemSelectedListener(bottomNavigationView: BottomNavigationView, listener: BottomNavigationView.OnNavigationItemSelectedListener) {
    bottomNavigationView.setOnNavigationItemSelectedListener(listener)
}