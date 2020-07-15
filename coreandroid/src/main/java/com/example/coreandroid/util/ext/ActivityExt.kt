package com.example.coreandroid.util.ext

import androidx.appcompat.app.AppCompatActivity
import com.example.coreandroid.R

fun AppCompatActivity.showDrawerHamburger() = supportActionBar?.apply {
    setDisplayHomeAsUpEnabled(true)
    setHomeAsUpIndicator(R.drawable.spotify_drawer_menu_background)
}
